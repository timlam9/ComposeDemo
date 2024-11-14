package com.example.composedemo.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


internal enum class BoxState {
    Collapsed,
    Expanded,
}

@Composable
internal fun ConcurrentAnimation(modifier: Modifier = Modifier) {
    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(0f) }

    LaunchedEffect("animationKey") {
        launch {
            alphaAnimation.animateTo(1f)
        }
        launch {
            yAnimation.animateTo(40f)
        }
    }

    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "transition")

    val alphaFloat by transition.animateFloat(label = "alpha") { state ->
        when (state) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> 1f
        }
    }

    val yFloat by transition.animateFloat(label = "y") { state ->
        when (state) {
            BoxState.Collapsed -> 1f
            BoxState.Expanded -> 100f
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
                translationY = yAnimation.value
            }
            .background(Color.Magenta)
            .fillMaxWidth()
            .height(100.dp)
    )
}