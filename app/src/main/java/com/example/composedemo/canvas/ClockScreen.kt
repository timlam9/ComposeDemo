package com.example.composedemo.canvas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
//        Gesture()
//        OAClock()
//        GeminiClock()
        ArcObjectAnimation(60f, 260f)
    }
}


@Composable
fun ArcObjectAnimation(
    startAngle: Float,
    endAngle: Float,
    durationMillis: Int = 1000,
    radius: Float = 100f,
) {
    var isAnimating by remember { mutableStateOf(false) }

    val progress = remember { Animatable(0f) }

    var arcCenter by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(key1 = isAnimating) {
        if (isAnimating) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = durationMillis)
            )
            isAnimating = false
        } else {
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = durationMillis)
            )
        }
    }

    val currentAngle = startAngle + (endAngle - startAngle) * progress.value
    val objectOffset = calculateObjectOffset(arcCenter, radius, currentAngle)

    Box(
        modifier = Modifier
            .size(200.dp)
            .onGloballyPositioned { coordinates ->
                arcCenter = coordinates.localToWindow(Offset.Zero)
            }
    ) {
        // Draw or place your object at objectOffset
        Box(
            modifier = Modifier
                .offset { IntOffset(objectOffset.x.toInt(), objectOffset.y.toInt()) }
                .clip(CircleShape)
                .background(Color.Blue)
                .size(10.dp)
        )
    }

    Button(onClick = { isAnimating = true }) {
        Text("Start Animation")
    }
}

fun calculateObjectOffset(center: Offset, radius: Float, angle: Float): Offset {
    val radians = angle * PI.toFloat() / 180f
    return Offset(
        center.x + radius * cos(radians),
        center.y + radius * sin(radians)
    )
}
