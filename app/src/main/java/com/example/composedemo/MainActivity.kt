package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.composedemo.stability.CounterRecomposition
import com.example.composedemo.stability.ListRecompositionTrackerScreen
import com.example.composedemo.stability.RecompositionTrackerViewModel
import com.example.composedemo.stability.SlidingRecompositionTrackerViewModel
import com.example.composedemo.stability.SlidingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val viewModel = RecompositionTrackerViewModel()
        val slidingViewModel = SlidingRecompositionTrackerViewModel()

        setContent {
            println("Set content recomposed")

            ListRecompositionTrackerScreen()
//            CounterRecomposition(viewModel = viewModel)
//            SlidingScreen(viewModel = slidingViewModel)
        }
    }
}


//            ComposeDemoTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    ComposeStability(innerPadding)
//                    ComposeEffects(modifier = Modifier.padding(innerPadding))
//                    AnimationsScreen(modifier = Modifier.padding(innerPadding))
//                    ClockScreen(modifier = Modifier.padding(innerPadding))
//                }
//            }