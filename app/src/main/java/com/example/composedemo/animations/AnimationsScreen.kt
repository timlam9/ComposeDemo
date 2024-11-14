package com.example.composedemo.animations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AnimationsScreen(modifier: Modifier = Modifier) {
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
        AnimatedVectorDrawable()

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


//spring: Physics-based animation, the default for all animations. You can change the stiffness or dampingRatio to achieve a different animation look and feel.
//tween (short for between): Duration-based animation, animates between two values with an Easing function.
//keyframes: Spec for specifying values at certain key points in an animation.
//repeatable: Duration-based spec that runs a certain number of times, specified by RepeatMode.
//infiniteRepeatable: Duration-based spec that runs forever.
//snap: Instantly snap