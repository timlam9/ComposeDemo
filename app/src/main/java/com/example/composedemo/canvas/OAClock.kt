package com.example.composedemo.canvas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private const val HOUR_STEP = 15
private const val DEGREES_OFFSET = 90
private const val THUMB_PADDING = 0.88f
private const val PADDING = 0.8f

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


    var isDragging by remember { mutableStateOf(false) }

    val initialThumbOffset by remember(radius) {
        mutableStateOf(
            Offset(
                x = center.x + (radius) * cos(0.getAngleInRadians()),
                y = center.y + (radius) * sin(0.getAngleInRadians()),
            )
        )
    }

    var currentThumbOffset: Offset by remember(initialThumbOffset) {
        mutableStateOf(
            calculateThumbPosition(
                angle = initialThumbOffset.calculateAngle(center),
                center = center,
                radius = radius,
                thumbSize = thumbSize,
            ),
        )
    }

    val animatedOffset = remember(radius) {
        Animatable(currentThumbOffset, Offset.VectorConverter)
    }


    val progress = remember { Animatable(0f) }
    var animatedEndAngle by remember { mutableFloatStateOf(0.getAngleInRadians()) }
    var animatedStartAngle by remember { mutableFloatStateOf(0.getAngleInRadians()) }

    var animatedThumbOffset by remember(initialThumbOffset) {
        mutableStateOf(currentThumbOffset)
    }

    LaunchedEffect(isDragging, animatedOffset.value) {
        if (isDragging) {
            animatedThumbOffset = animatedOffset.value
        } else {
            println("TAGARA | NOT DRAGGING: ${animatedOffset.value}")
        }
    }

    LaunchedEffect(isDragging, animatedStartAngle, animatedEndAngle, progress.value) {
        if (isDragging) {
            println("TAGARA | DRAGGING: $animatedStartAngle, $animatedEndAngle, ${progress.value}")
        } else {
            fun calculateAnimatedThumbOffset(
                startAngle: Float,
                endAngle: Float,
                progress: Float,
            ): Offset {
                val currentAngle = startAngle + (endAngle - startAngle) * progress

                val objectOffset = calculateSnappedThumbPosition(
                    snappedAngle = currentAngle.toRadians(),
                    center = center,
                    radius = radius,
                    thumbSize = thumbSize,
                )

                if (progress == 1f) animatedStartAngle = animatedEndAngle

                return objectOffset
            }

            animatedThumbOffset = calculateAnimatedThumbOffset(
                startAngle = animatedStartAngle,
                endAngle = animatedEndAngle,
                progress = progress.value,
            )
        }
    }

    Canvas(
        modifier = modifier
            .size(clockWidth)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .pointerInput(Unit) {
                coroutineScope {
                    detectDragGestures(
                        onDragEnd = {
                            val snappedAngle = currentThumbOffset.calculateSnappedAngle(center)

                            calculateSnappedThumbPosition(
                                snappedAngle = snappedAngle,
                                center = center,
                                radius = radius,
                                thumbSize = thumbSize,
                            ).also { position ->

                                animatedEndAngle = position
                                    .calculateSnappedAngle(center)
                                    .toDegrees()
                                animatedStartAngle = animatedEndAngle

                                launch {
                                    progress.snapToValue()
                                }

                                launch {
                                    currentThumbOffset = position
                                    animatedOffset.animateTo(position)



                                    isDragging = false
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        isDragging = true

                        val newPosition = currentThumbOffset + dragAmount

                        currentThumbOffset = calculateThumbPosition(
                            angle = newPosition.calculateAngle(center),
                            center = center,
                            radius = radius,
                            thumbSize = thumbSize,
                        ).also { position ->
                            launch { animatedOffset.animateTo(position) }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        awaitPointerEventScope {
                            val touchedPosition = awaitFirstDown().position

                            touchedPosition
                                .calculateSnappedAngle(center)
                                .toDegrees()
                                .also { snappedAngle ->
                                    animatedEndAngle = snappedAngle

                                    calculateSnappedThumbPosition(
                                        snappedAngle = snappedAngle.toRadians(),
                                        center = center,
                                        radius = radius,
                                        thumbSize = thumbSize
                                    ).also {
                                        currentThumbOffset = it
                                        launch {
                                            animatedOffset.snapTo(currentThumbOffset)
                                        }
                                    }

                                    launch {
                                        progress.startAnimationFromStart()
                                    }
                                }
                        }
                    }

                }
            },
    ) {
        val circlePath = Path().apply { addOval(Rect(animatedThumbOffset, thumbSize)) }

        drawText(
            textMeasurer = textMeasurer,
            text = "Drag: $isDragging",
            style = textStyle.copy(
                fontSize = 24.sp,
                color = if (isDragging) Color.Green else Color.Red
            ),
            topLeft = Offset(x = center.x / 2, y = center.y / 3)
        )

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
            end = animatedThumbOffset,
            strokeWidth = 10f
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize,
            center = animatedThumbOffset,
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize,
            center = animatedThumbOffset,
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

private suspend fun Animatable<Float, AnimationVector1D>.startAnimationFromStart() {
    snapTo(0f)
    animateTo(
        targetValue = 1f,
        animationSpec = tween(delayMillis = 250),
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.snapToValue() {
    snapTo(1f)
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

            val x = center.x - textWidth / 2 + (radius * PADDING) * cos(i.getAngleInRadians())
            val y = center.y - textHeight / 2 + (radius * PADDING) * sin(i.getAngleInRadians())

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


private fun Offset.calculateAngle(center: Offset): Float {
    return atan2(y - center.y, x - center.x)
}

private fun Offset.calculateSnappedAngle(center: Offset, step: Int = HOUR_STEP): Float {
    return calculateAngle(center).toDegrees().roundToInt() / step * step.toRadians()
}

private fun calculateThumbPosition(
    angle: Float,
    center: Offset,
    radius: Float,
    thumbSize: Float,
): Offset {
    val thumbRadius = (radius * THUMB_PADDING) - thumbSize / 2

    return Offset(
        center.x + thumbRadius * cos(angle),
        center.y + thumbRadius * sin(angle)
    )
}

private fun calculateSnappedThumbPosition(
    snappedAngle: Float,
    center: Offset,
    radius: Float,
    thumbSize: Float,
): Offset {
    val thumbRadius = (radius * THUMB_PADDING) - thumbSize / 2

    return Offset(
        center.x + thumbRadius * cos(snappedAngle),
        center.y + thumbRadius * sin(snappedAngle)
    )
}


private fun Int.getAngleInRadians(
    step: Int = HOUR_STEP,
    offset: Int = DEGREES_OFFSET,
): Float {
    return ((this * step) - offset).toRadians()
}

private fun getAngleFromCircleInDegrees(
    center: Offset,
    point: Offset,
): Float {
    val deltaX = point.x - center.x
    val deltaY = point.y - center.y
    val radians = atan2(deltaY, deltaX)

    var degrees = radians.toDegrees() + DEGREES_OFFSET

    if (degrees < 0) degrees += 360
    if (degrees == 24f) degrees = 0f

    return degrees
}


fun Int.toDegrees(): Float = (this * (180 / PI)).toFloat()

fun Double.toDegrees(): Float = (this * (180 / PI)).toFloat()

fun Float.toDegrees(): Float = (this * (180 / PI)).toFloat()

fun Int.toRadians(): Float = (this * (PI / 180)).toFloat()

fun Double.toRadians(): Float = (this * (PI / 180)).toFloat()

fun Float.toRadians(): Float = (this * (PI / 180)).toFloat()
