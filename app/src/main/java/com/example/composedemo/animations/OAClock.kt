package com.example.composedemo.animations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private val hourStep = 30
private val textPadding = 100f

private fun Int.getAngle(): Float = ((this.toFloat() * hourStep) - 90) * (PI / 180).toFloat()

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

    var radius by remember { mutableFloatStateOf(0f) }
    val center by remember(radius) { mutableFloatStateOf(radius) }
    val thumbSize by remember(radius) { mutableFloatStateOf(radius / 6) }

    fun Int.getTextCenter(): Float {
        return if (this < 10) singleNumberTextLayoutResult.size.width / 2f else numberTextLayoutResult.size.width / 2f
    }

    fun Int.getXPosition(padding: Float, objectCenter: Float): Float {
        return center + (radius - padding - objectCenter) * cos(getAngle())
    }

    fun Int.getYPosition(padding: Float, objectCenter: Float): Float {
        return center + (radius - padding - objectCenter) * sin(getAngle())
    }

    var currentThumbOffset by remember(radius) {
        mutableStateOf(
            Offset(
                x = 0.getXPosition(textPadding, thumbSize / 2),
                y = 0.getYPosition(textPadding, thumbSize / 2)
            )
        )
    }


    Canvas(
        modifier = modifier
            .clip(CircleShape)
            .background(color = backgroundColor)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        currentThumbOffset =
                            calculateThumbPosition(currentThumbOffset, it, center, radius)
                    },
                    onDragEnd = {}
                ) { change, dragAmount ->
                    change.consume()
                    currentThumbOffset =
                        calculateThumbPosition(currentThumbOffset, dragAmount, center, radius)
                }
            },
    ) {
        if (radius == 0f) {
            radius = size.width / 2
        }

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

        drawLine(
            color = Color.Red,
            start = Offset(center, center),
            end = currentThumbOffset,
            strokeWidth = 10f
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize,
            center = currentThumbOffset,
        )

        val circlePath = Path().apply {
            addOval(Rect(currentThumbOffset, thumbSize))
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

private fun calculateThumbPosition(
    shapePosition: Offset,
    dragAmount: Offset,
    center: Float,
    radius: Float,
): Offset {
    var shapePosition1 = shapePosition
    val newPosition = shapePosition1 + dragAmount
    val angle = atan2(newPosition.y - center, newPosition.x - center)

    shapePosition1 = Offset(
        center + (radius - radius / 5) * cos(angle),
        center + (radius - radius / 5) * sin(angle)
    )
    return shapePosition1
}