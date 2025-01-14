package com.example.composedemo.compose.animations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
internal fun AnimateAsState(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        label = "alpha"
    )

    val animatedColor by animateColorAsState(
        if (visible) Color.Green else Color.Blue,
        label = "color"
    )

    Box(
        modifier = modifier
            .size(200.dp)
            .drawBehind { drawRect(animatedColor) }
            .graphicsLayer { alpha = animatedAlpha }
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Green)
            .clickable(onClick = { visible = !visible })
    )
}