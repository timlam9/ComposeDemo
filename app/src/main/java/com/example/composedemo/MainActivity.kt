package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.composedemo.stability.CounterRecomposition
import com.example.composedemo.stability.RecompositionTrackerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val viewModel = RecompositionTrackerViewModel()

        setContent {
            println("Set content recomposed")

            CounterRecomposition(viewModel = viewModel)
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