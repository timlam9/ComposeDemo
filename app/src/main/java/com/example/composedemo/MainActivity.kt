package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.composedemo.animations.AnimationsScreen
import com.example.composedemo.canvas.ClockScreen
import com.example.composedemo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    ComposeStability(innerPadding)
//                    ComposeEffects(modifier = Modifier.padding(innerPadding))
//                    AnimationsScreen(modifier = Modifier.padding(innerPadding))
                    ClockScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
