package com.example.composedemo.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
internal fun StartSequentialAnimation(modifier: Modifier = Modifier) {
    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnimation.animateTo(1f, animationSpec = tween(3000))
        yAnimation.animateTo(100f)
        yAnimation.animateTo(100f, animationSpec = tween(6000))
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
                translationY = yAnimation.value
            }
            .background(Color.Cyan)
            .fillMaxWidth()
            .height(100.dp)
    )
}