package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.composedemo.compose.canvas.ClockScreen
import com.example.composedemo.compose.stability.CounterRecomposition
import com.example.composedemo.compose.stability.ListRecompositionTrackerScreen
import com.example.composedemo.compose.stability.RecompositionTrackerViewModel
import com.example.composedemo.compose.stability.SlidingRecompositionTrackerViewModel
import com.example.composedemo.compose.stability.SlidingScreen
import com.example.composedemo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val viewModel = RecompositionTrackerViewModel()
        val slidingViewModel = SlidingRecompositionTrackerViewModel()

        setContent {
            println("Set content recomposed")

            ComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClockScreen(modifier = Modifier.padding(innerPadding))
                }
            }

//            ListRecompositionTrackerScreen()
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