package com.example.composedemo.animations

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun DarkHole(modifier: Modifier = Modifier) {
    var pointerOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    val localDensity = LocalDensity.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxSize()
            .pointerInput("dragging") {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    pointerOffset += dragAmount
                }
            }
            .onSizeChanged {
                val startingHeight = with(localDensity) { 100.dp.toPx() }

                pointerOffset = Offset(it.width / 2f, startingHeight)
            }
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.radialGradient(
                        listOf(Color.Transparent, Color.Black),
                        center = pointerOffset,
                        radius = 100.dp.toPx(),
                    )
                )
            }
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "That's all folks!", style = MaterialTheme.typography.titleLarge)
        Text(text = "Thank you for your time!", style = MaterialTheme.typography.titleLarge)
        Text(text = "Compose only!!!", style = MaterialTheme.typography.titleLarge)
    }
}
