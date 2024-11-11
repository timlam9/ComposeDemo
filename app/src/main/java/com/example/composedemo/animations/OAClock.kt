package com.example.composedemo.animations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import kotlin.math.*

@Composable
fun OAClock(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    thumbColor: Color = Color.Red,
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray)

    val singleNumberTextLayoutResult = remember { textMeasurer.measure("0", textStyle) }
    val numberTextLayoutResult = remember { textMeasurer.measure("22", textStyle) }

    var center by remember { mutableFloatStateOf(0f) }
    var radius by remember { mutableFloatStateOf(0f) }

    val hourStep = 30
    val textPadding = 100f

    fun Int.getAngle(): Float = ((this.toFloat() * hourStep) - 90) * (PI / 180).toFloat()

    fun Int.getTextCenter(): Float {
        return if (this < 10) singleNumberTextLayoutResult.size.width / 2f else numberTextLayoutResult.size.width / 2f
    }

    fun Int.getXPosition(padding: Float, objectCenter: Float): Float {
        return center + (radius - padding - objectCenter) * cos(getAngle())
    }

    fun Int.getYPosition(padding: Float, objectCenter: Float): Float {
        return center + (radius - padding - objectCenter) * sin(getAngle())
    }

    var offsetX by remember { mutableFloatStateOf(center) }
    var offsetY by remember { mutableFloatStateOf(center) }

    var shapePosition by remember { mutableStateOf(Offset(500f, 500f)) }

    fun Offset.distanceTo(other: Offset) = sqrt((other.x - x).pow(2) + (other.y - y).pow(2))

    Canvas(
        modifier = modifier
            .clip(CircleShape)
            .background(color = backgroundColor)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val newPosition = shapePosition + dragAmount
                    val distanceToCenter = newPosition.distanceTo(Offset(center, center))

                    shapePosition = when (distanceToCenter) {
                        radius -> newPosition
                        else -> {
                            val angle = atan2(
                                newPosition.y - center,
                                newPosition.x - center
                            )
                            Offset(
                                center + (radius - radius / 5) * cos(angle),
                                center + (radius - radius / 5) * sin(angle)
                            )
                        }
                    }

                    offsetX = shapePosition.x
                    offsetY = shapePosition.y

                    change.consume()
                }
            },
    ) {
        radius = size.width / 2
        center = radius

        var hour = 0
        for (i in 0..11) {
            drawText(
                textMeasurer = textMeasurer,
                text = hour.toString(),
                style = textStyle,
                topLeft = Offset(
                    x = i.getXPosition(textPadding, i.getTextCenter()),
                    y = i.getYPosition(textPadding, i.getTextCenter()),
                )
            )
            hour += 2
        }

        drawCircle(
            color = thumbColor,
            radius = radius / 40,
            center = Offset(x = center, y = center),
        )

        val thumbSize = radius / 6
        drawLine(
            color = Color.Red,
            start = Offset(center, center),
            end = Offset(offsetX, offsetY),
            strokeWidth = 10f
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize,
            center = Offset(x = offsetX, y = offsetY),
        )

        val circlePath = Path().apply {
            addOval(Rect(Offset(x = offsetX, y = offsetY), thumbSize))
        }

        clipPath(circlePath) {
            var hour2 = 0
            for (i in 0..11) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = hour2.toString(),
                    style = textStyle.copy(Color.White),
                    topLeft = Offset(
                        x = i.getXPosition(textPadding, i.getTextCenter()),
                        y = i.getYPosition(textPadding, i.getTextCenter()),
                    )
                )
                hour2 += 2
            }
        }
    }
}