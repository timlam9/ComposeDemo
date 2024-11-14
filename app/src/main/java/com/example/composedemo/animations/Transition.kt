package com.example.composedemo.animations

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun Transition(modifier: Modifier = Modifier) {
    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "transition")

    val color by transition.animateColor(label = "color") { state ->
        when (state) {
            BoxState.Collapsed -> Color.Red
            BoxState.Expanded -> Color.Green
        }
    }

    val borderWidth by transition.animateDp(label = "borderWidth") { state ->
        when (state) {
            BoxState.Collapsed -> 10.dp
            BoxState.Expanded -> 1.dp
        }
    }

    Button(
        onClick = {
            currentState = when (currentState) {
                BoxState.Collapsed -> BoxState.Expanded
                BoxState.Expanded -> BoxState.Collapsed
            }
        },
        modifier = modifier
            .clip(CircleShape)
            .drawBehind {
                drawRect(color = color)
            }
            .border(borderWidth, Color.Blue, shape = CircleShape)
            .padding(20.dp)
    ) {
        Text("updateTransition")
    }
}