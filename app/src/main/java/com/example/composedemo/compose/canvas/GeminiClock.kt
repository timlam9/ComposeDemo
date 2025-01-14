package com.example.composedemo.compose.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GeminiClock(modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray)
    val numberTextLayoutResult = remember { textMeasurer.measure("22", textStyle) }

    Canvas(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(20.dp)
            .background(Color.LightGray)
            .size(200.dp) // Adjust size as needed
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = centerX

        // Draw clock circle
        drawCircle(
            color = Color.Black,
            center = Offset(centerX, centerY),
            radius = radius,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw hour markers
        for (hour in 0..11) {
            val angle = (hour * 30 - 90) * PI / 180 // Convert hour to angle
            val x = centerX - 20 + (radius - 50) * cos(angle).toFloat()
            val y = centerY - 30 + (radius - 50) * sin(angle).toFloat()

            drawText(
                textMeasurer = textMeasurer,
                text = hour.toString(),
                style = textStyle,
                topLeft = Offset(
                    x = x,
                    y = y,
                )
            )
        }
    }
}