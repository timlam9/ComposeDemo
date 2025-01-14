package com.example.composedemo.compose.effects

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
internal fun ComposeEffects(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
//        var bugs = remember { 0 }
//        var bugs by mutableIntStateOf(0)
        var bugs by remember { mutableIntStateOf(0) }

        // Do I need to pass a key to remember?
        val crash by remember {
            derivedStateOf {
                when (bugs) {
                    5 -> true
                    else -> false
                }
            }
        }


        DisposableEffect(bugs) {
            Log.d("COMPOSE_EFFECTS", "DisposableEffect: $bugs")

            onDispose {
                Log.d("COMPOSE_EFFECTS", "onDispose")
            }
        }
//                BackHandler { }

        LaunchedEffect(bugs) {

            Log.d("COMPOSE_EFFECTS", "LaunchedEffect: $bugs")
        }

        SideEffect {
            Log.d("COMPOSE_EFFECTS", "SideEffect: $bugs")
        }

        val coroutineScope = rememberCoroutineScope()

        val producedState = produceState(
            initialValue = bugs,
            key1 = bugs,
            producer = {
                Log.d("COMPOSE_EFFECTS", "produceState: $bugs")
                value = bugs
            }
        )

        Text(
            text = "Compose effects",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        )
        Text(
            text = "bugs: $bugs",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 20.dp),
        )
        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 40.dp),
            onClick = { bugs++ },
            content = { Text("New bug") },
        )

        Log.d("COMPOSE_EFFECTS", "re-composition\n")

        if (crash) {
//            throw Exception("Exception")
            Log.d("COMPOSE_EFFECTS", "Crash \n")
        }
    }
}
