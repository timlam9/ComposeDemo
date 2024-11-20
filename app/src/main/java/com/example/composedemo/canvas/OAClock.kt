package com.example.composedemo.canvas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.style.TextAlign
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OAClockDialog(
    displayDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    if (displayDialog) {
        BasicAlertDialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                color = Color.White,
                content = { ClockDialogContent(onDismiss = onDismiss, onConfirm = onConfirm) },
            )
        }
    }
}

@Composable
fun ClockDialogContent(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var time by remember { mutableStateOf("0") }
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Select time",
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Black)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Box(
                modifier = Modifier
                    .width(width = 100.dp)
                    .clickable(onClick = {})
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = Color.Black)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 6.dp, top = 20.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 52.sp,
                    ),
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = ":",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 62.sp,
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(width = 100.dp)
                    .clickable(onClick = {})
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = Color.LightGray)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 6.dp, top = 20.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = "20",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 52.sp,
                    ),
                )
            }
        }
        OAClock(onHourSelected = { time = it.toString() })
        Row {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(onClick = { onConfirm(time) }) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                )
            }
        }
    }
}

@Composable
fun OAClock(
    onHourSelected: (hour: Int) -> Unit,
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

    // Drag animation properties
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

    // Touch animation properties
    val progress = remember { Animatable(0f) }
    var animatedEndAngle by remember { mutableFloatStateOf(0.getAngleInRadians()) }
    var animatedStartAngle by remember { mutableFloatStateOf(0.getAngleInRadians()) }

    // Final animation value
    var animatedThumbOffset by remember(initialThumbOffset) {
        mutableStateOf(currentThumbOffset)
    }

    // Update drag animation
    LaunchedEffect(isDragging, animatedOffset.value) {
        if (isDragging) {
            animatedThumbOffset = animatedOffset.value
        }
    }

    // Update touch animation
    LaunchedEffect(isDragging, animatedStartAngle, animatedEndAngle, progress.value) {
        if (!isDragging) {
            fun calculateAnimatedThumbOffset(
                startAngle: Float,
                endAngle: Float,
                progress: Float,
            ): Offset {
                val currentAngle = startAngle + (endAngle - startAngle) * progress

                val objectOffset = calculateThumbPosition(
                    angle = currentAngle.toRadians(),
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

    fun Offset.getSelectedHour(): Int {
        val angle = getAngleFromCircleInDegrees(center, this)
        val number = (angle / HOUR_STEP).roundToInt()

        return number
    }

    var selectedHour by remember(initialThumbOffset) {
        mutableIntStateOf(initialThumbOffset.getSelectedHour())
    }

    LaunchedEffect(animatedOffset.value) {
        val newHour = animatedOffset.value.getSelectedHour()

        if (newHour != selectedHour) {
            selectedHour = newHour
            onHourSelected(selectedHour)
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

                            calculateThumbPosition(
                                angle = snappedAngle,
                                center = center,
                                radius = radius,
                                thumbSize = thumbSize,
                            ).also { position ->

                                // Update angles in order to be aligned for the touch animation
                                animatedEndAngle = position
                                    .calculateSnappedAngle(center)
                                    .toDegrees()
                                animatedStartAngle = animatedEndAngle

                                launch { progress.snapToValue() }

                                // Update the thumb position to snap to hours step
                                launch {
                                    currentThumbOffset = position
                                    animatedOffset.animateTo(position)

                                    isDragging = false
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
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
                    )
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

                                    // Update thumb offset in order to be aligned for the drag animation
                                    calculateThumbPosition(
                                        angle = snappedAngle.toRadians(),
                                        center = center,
                                        radius = radius,
                                        thumbSize = thumbSize
                                    ).also {
                                        currentThumbOffset = it

                                        launch {
                                            animatedOffset.snapTo(currentThumbOffset)
                                        }
                                    }

                                    // Animate with arc motion to end/touched position
                                    launch { progress.startAnimationFromStart() }
                                }
                        }
                    }

                }
            },
    ) {
        val circlePath = Path().apply { addOval(Rect(animatedThumbOffset, thumbSize)) }

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

private fun Int.getAngleInRadians(
    step: Int = HOUR_STEP,
    offset: Int = DEGREES_OFFSET,
): Float {
    return ((this * step) - offset).toRadians()
}

private fun <T : Number> T.toDegrees(): Float = (this.toFloat() * (180 / PI)).toFloat()

private fun <T : Number> T.toRadians(): Float = (this.toFloat() * (PI / 180)).toFloat()


private fun getAngleFromCircleInDegrees(
    center: Offset,
    point: Offset,
): Float {
    val deltaX = point.x - center.x
    val deltaY = point.y - center.y
    val radians = atan2(deltaY, deltaX)

    var degrees = radians.toDegrees()
    degrees += 90f

    if (degrees < 0) degrees += 360

    if (degrees == 24f) degrees = 0f

    return degrees
}
