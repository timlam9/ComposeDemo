package com.example.composedemo.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composedemo.R
import kotlinx.coroutines.launch

@Composable
fun Animations(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // 1. AnimatedVectorDrawable
//        AnimatedVectorDrawable()

        // 2. rememberInfiniteTransition
        InfiniteAnimation()

        // Changing between multiple composables that have different content
        // 3. Navigation -> composable() with enterTransition, exitTransition
        NavigationTransition()

        // 4. AnimatedContent, Crossfade, Pager
        UiStateContentAnimation()

        // 5. AnimatedVisibility, animateFloatAsState with Modifier.alpha
        VisibilityAnimation()

        // 6. Modifier.animateContentSize
        ContentSizeAnimation()

        // 7. animateItemPlacement

        // Multiple properties
        // 8. Independent of each other - animate*AsState (TextMotion.Animated) for predefined target values, for multiple properties or not
        AnimateAsState()

        // 9. Start the same time - updateTransition with AnimatedVisibility, animateFloat...
        Transition()

        // 10. Animatable with animateTo (also for gesture animations)
        StartSequentialAnimation()
        ConcurrentAnimation()

        // 11. AnimationState or animate for one shot animation
        OneTimeAnimation()

        OffsetLayoutAnimation()
        CounterAnimation()
    }
}

@Composable
private fun OneTimeAnimation() {
    // Create a mutable state for alpha, and update it in the animation.
    val alpha = remember { mutableFloatStateOf(1f) }
    LaunchedEffect(Unit) {
        // Animate from 1f to 0f using an infinitely repeating animation
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(10000),
        ) { value, /* velocity */ _ ->
            // Update alpha mutable state with the current animation value
            alpha.floatValue = value
        }
    }
    Box(
        Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(alpha = alpha.floatValue),
            tint = Color.Red
        )
    }
}

@Composable
private fun StartSequentialAnimation(modifier: Modifier = Modifier) {
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

@Composable
fun ConcurrentAnimation(modifier: Modifier = Modifier) {
    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(0f) }

    LaunchedEffect("animationKey") {
        launch {
            alphaAnimation.animateTo(1f)
        }
        launch {
            yAnimation.animateTo(40f)
        }
    }

    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "transition")

    val alphaFloat by transition.animateFloat(label = "alpha") { state ->
        when (state) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> 1f
        }
    }

    val yFloat by transition.animateFloat(label = "y") { state ->
        when (state) {
            BoxState.Collapsed -> 1f
            BoxState.Expanded -> 100f
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
                translationY = yAnimation.value
            }
            .background(Color.Magenta)
            .fillMaxWidth()
            .height(100.dp)
    )
}

@Composable
private fun NavigationTransition(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = "landing",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("landing") {
            Text("Landing screen")
        }
        composable(
            "detail/{photoUrl}",
            arguments = listOf(navArgument("photoUrl") { type = NavType.StringType }),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            Text("Details screen: ${backStackEntry.arguments?.getString("photoUrl")}")
        }
    }
}

@Composable
private fun VisibilityAnimation(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(visible) {
        Box(
            modifier
                .drawBehind { drawRect(Color.Blue) }
                .fillMaxWidth()
                .height(80.dp)
                .clickable(onClick = { visible = !visible })
        )
    }
}

@Composable
private fun AnimateAsState(modifier: Modifier = Modifier) {
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

@Composable
private fun ContentSizeAnimation(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color.Blue)
            .animateContentSize()
            .height(if (expanded) 400.dp else 200.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                expanded = !expanded
            }

    )
}


private enum class BoxState {
    Collapsed,
    Expanded,
}

@Composable
private fun Transition(modifier: Modifier = Modifier) {
    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "transition")

    val color by transition.animateColor(label = "color") { state ->
        when (state) {
            BoxState.Collapsed -> Color.Red
            BoxState.Expanded -> Color.Green
        }
    }

    val borderWidth by transition.animateDp(label = "borderWidth") { state ->
        when (state) {
            BoxState.Collapsed -> 10.dp
            BoxState.Expanded -> 1.dp
        }
    }

    Button(
        onClick = {
            currentState = when (currentState) {
                BoxState.Collapsed -> BoxState.Expanded
                BoxState.Expanded -> BoxState.Collapsed
            }
        },
        modifier = modifier
            .clip(CircleShape)
            .drawBehind {
                drawRect(color = color)
            }
            .border(borderWidth, Color.Blue, shape = CircleShape)
            .padding(20.dp)
    ) {
        Text("updateTransition")
    }
}


sealed interface UiState {

    object Loading : UiState
    object Loaded : UiState
    object Error : UiState
}

@Composable
private fun UiStateContentAnimation(modifier: Modifier = Modifier) {
    var state: UiState by remember {
        mutableStateOf(UiState.Loading)
    }
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(1000)
            ) togetherWith fadeOut(animationSpec = tween(1000))
        },
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            state = when (state) {
                UiState.Loading -> UiState.Loaded
                UiState.Loaded -> UiState.Error
                UiState.Error -> UiState.Loading
            }
        },
        label = "Animated Content"
    ) { targetState ->
        when (targetState) {
            UiState.Loading -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Loading")
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator()
                }
            }

            UiState.Loaded -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Loaded data")
                }
            }

            UiState.Error -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Error", color = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun InfiniteAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Magenta,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )

    val color by infiniteTransition.animateColor(
        initialValue = Color.Green,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(color)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BasicText(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                },
            text = "Infinite transition",
            color = { animatedColor },
            style = LocalTextStyle.current.copy(textMotion = TextMotion.Animated),
        )
    }
}


@Composable
private fun OffsetLayoutAnimation(modifier: Modifier = Modifier) {
    var toggled by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable(indication = null, interactionSource = interactionSource) {
                toggled = !toggled
            }
    ) {
        val offsetTarget = if (toggled) {
            IntOffset(150, 150)
        } else {
            IntOffset.Zero
        }
        val offset = animateIntOffsetAsState(
            targetValue = offsetTarget, label = "offset"
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
        )
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val offsetValue = if (isLookingAhead) offsetTarget else offset.value
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width + offsetValue.x, placeable.height + offsetValue.y) {
                        placeable.placeRelative(offsetValue)
                    }
                }
                .size(100.dp)
                .background(Color.Green)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
        )
    }
}

@Composable
private fun CounterAnimation(modifier: Modifier = Modifier) {
    var count by remember { mutableIntStateOf(0) }

    AnimatedContent(
        targetState = count,
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                SizeTransform(clip = false)
            )
        }, label = "animated content"
    ) { targetCount ->
        Text(
            text = "$targetCount",
            modifier = modifier.clickable { if (count < 10) count++ else count-- })
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedVectorDrawable() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.ic_launcher_foreground)
    var atEnd by remember { mutableStateOf(false) }
    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = "Timer",
        modifier = Modifier.background(Color.Green).clickable {
            atEnd = !atEnd
        },
        contentScale = ContentScale.Crop
    )
}

//spring: Physics-based animation, the default for all animations. You can change the stiffness or dampingRatio to achieve a different animation look and feel.
//tween (short for between): Duration-based animation, animates between two values with an Easing function.
//keyframes: Spec for specifying values at certain key points in an animation.
//repeatable: Duration-based spec that runs a certain number of times, specified by RepeatMode.
//infiniteRepeatable: Duration-based spec that runs forever.
//snap: Instantly snap