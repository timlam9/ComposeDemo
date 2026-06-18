package com.example.composedemo.compose.canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.skydoves.compose.stability.runtime.TraceRecomposition

@TraceRecomposition
@Composable
fun ClockScreen(modifier: Modifier = Modifier) {
    var displayDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
//        Gesture()
//        GeminiClock()
//        OAClock(onHourSelected = { println("Hour selected: $it") })

        Button(onClick = { displayDialog = true }) {
            Text("show dialog")
        }

        OAClockDialog(
            displayDialog = displayDialog,
            onDismiss = { displayDialog = false },
            onConfirm = { displayDialog = false }
        )
    }
}
