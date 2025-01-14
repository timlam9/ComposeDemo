package com.example.composedemo.compose.animations

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
internal fun OneTimeAnimation() {
    // Create a mutable state for alpha, and update it in the animation.
    val alpha = remember { mutableFloatStateOf(1f) }
    LaunchedEffect(Unit) {
        // Animate from 1f to 0f using an infinitely repeating animation
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(10000),
        ) { value, /* velocity */ _ ->
            // Update alpha mutable state with the current animation value
            alpha.floatValue = value
        }
    }
    Box(
        Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(alpha = alpha.floatValue),
            tint = Color.Red
        )
    }
}