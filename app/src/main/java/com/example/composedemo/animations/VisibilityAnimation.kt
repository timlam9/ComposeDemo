package com.example.composedemo.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun VisibilityAnimation(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible) {
            Box(
                modifier
                    .drawBehind { drawRect(Color.Blue) }
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable(onClick = { visible = !visible })
            )
        }

        Button({ visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }
    }
}