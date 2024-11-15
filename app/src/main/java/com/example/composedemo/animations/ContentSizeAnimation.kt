package com.example.composedemo.animations

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
internal fun ContentSizeAnimation(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var moved by remember { mutableStateOf(false) }
    val pxToMove = with(LocalDensity.current) { 30.dp.toPx().roundToInt() }
    val offset by animateIntOffsetAsState(
        targetValue = if (moved) { IntOffset(pxToMove, pxToMove) } else { IntOffset.Zero },
        label = "offset"
    )

    Column(modifier = modifier.background(Color.Red)) {
        Box(
            modifier = modifier
                .background(Color.Blue)
                .animateContentSize()
                .height(if (expanded) 400.dp else 200.dp)
                .width(100.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = !expanded
                }

        )
        Box(
            modifier = Modifier
                .offset { offset }
                .background(Color.Green)
                .size(100.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    moved = !moved
                }
        )
        Box(
            modifier = Modifier
                .background(Color.Blue)
                .size(100.dp)
        )
    }
}