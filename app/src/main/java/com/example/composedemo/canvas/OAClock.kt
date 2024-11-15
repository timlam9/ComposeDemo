package com.example.composedemo.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private const val HOUR_STEP = 15

@Composable
fun OAClock(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    thumbColor: Color = Color.Red,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val clockWidth = screenWidth - screenWidth / 3

    val clockWidthInPixels = with(LocalDensity.current) { clockWidth.toPx() }
    val radius = clockWidthInPixels / 2f

    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray)

    val singleNumberTextLayoutResult = remember { textMeasurer.measure("0", textStyle) }
    val numberTextLayoutResult = remember { textMeasurer.measure("22", textStyle) }

    val center by remember { mutableStateOf(Offset(radius, radius)) }
    val thumbSize by remember { mutableFloatStateOf(radius / 6) }

    var currentThumbOffset: Offset by remember(radius) {
        mutableStateOf(
            calculateThumbPosition(
                currentOffset = Offset(
                    x = center.x + (radius) * cos(0.getAngle()),
                    y = center.y + (radius) * sin(0.getAngle()),
                ),
                dragAmount = Offset.Zero,
                center = center,
                radius = radius,
                thumbSize = thumbSize,
            ) ?: Offset.Zero
        )
    }

    Canvas(
        modifier = modifier
            .size(clockWidth)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    calculateThumbPosition(
                        currentOffset = currentThumbOffset,
                        dragAmount = dragAmount,
                        center = center,
                        radius = radius,
                        thumbSize = thumbSize,
                    )?.let { currentThumbOffset = it }
                }
            },
    ) {
        val circlePath = Path().apply { addOval(Rect(currentThumbOffset, thumbSize)) }

        drawHours(
            singleNumberTextLayoutResult = singleNumberTextLayoutResult,
            numberTextLayoutResult = numberTextLayoutResult,
            textMeasurer = textMeasurer,
            textStyle = textStyle,
            center = center,
            radius = radius
        )

        drawCircle(
            color = thumbColor,
            radius = radius / 40,
            center = center,
        )

        drawLine(
            color = thumbColor,
            start = center,
            end = currentThumbOffset,
            strokeWidth = 10f
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize,
            center = currentThumbOffset,
        )

        clipPath(circlePath) {
            drawHours(
                singleNumberTextLayoutResult = singleNumberTextLayoutResult,
                numberTextLayoutResult = numberTextLayoutResult,
                textMeasurer = textMeasurer,
                textStyle = textStyle.copy(Color.White),
                center = center,
                radius = radius
            )
        }
    }
}

private fun DrawScope.drawHours(
    singleNumberTextLayoutResult: TextLayoutResult,
    numberTextLayoutResult: TextLayoutResult,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    center: Offset,
    radius: Float,
) {
    var hour = 0
    for (i in 0..22) {
        if (i.rem(2) == 0) {
            val textWidth = when {
                i < 10 -> singleNumberTextLayoutResult.size.width.toFloat()
                else -> numberTextLayoutResult.size.width.toFloat()
            }
            val textHeight = when {
                i < 10 -> singleNumberTextLayoutResult.size.height.toFloat()
                else -> numberTextLayoutResult.size.height.toFloat()
            }

            val x = center.x - textWidth / 2 + (radius * 0.8f) * cos(i.getAngle())
            val y = center.y - textHeight / 2 + (radius * 0.8f) * sin(i.getAngle())

            drawText(
                textMeasurer = textMeasurer,
                text = hour.toString(),
                style = textStyle,
                topLeft = Offset(x = x, y = y)
            )
        }

        hour += 1
    }
}


private var previousNumber = 0

private fun calculateThumbPosition(
    currentOffset: Offset,
    dragAmount: Offset,
    center: Offset,
    radius: Float,
    thumbSize: Float,
): Offset? {
    if (radius == 0f) return Offset.Zero

    val newPosition = currentOffset + dragAmount
    val angle = atan2(newPosition.y - center.y, newPosition.x - center.x)
    val thumbRadius = (radius * 0.88f) - thumbSize / 2

    var result: Offset? = null

    val currentAngle = getAngleFromCircle(center, newPosition).roundToInt()

    val number = (currentAngle / HOUR_STEP)

    if (number != previousNumber) {
        println("TAGARA: Number -> $number")
        previousNumber = number
    }

    result = Offset(
        center.x + thumbRadius * cos(angle),
        center.y + thumbRadius * sin(angle)
    )

    return result
}


private fun Int.getAngle(): Float = ((this * HOUR_STEP) - 90) * (PI / 180).toFloat()

private fun getAngleFromCircle(center: Offset, point: Offset): Float {
    val deltaX = point.x - center.x
    val deltaY = point.y - center.y
    val radians = atan2(deltaY, deltaX)

    var degrees = radians * 180 / PI
    degrees += 90f

    if (degrees < 0) degrees += 360f
    if (degrees == 24.0) degrees = 0.0

    return degrees.toFloat()
}
