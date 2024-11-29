package com.example.composedemo.stability

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

// Understanding the recomposition scope
// https://proandroiddev.com/6-jetpack-compose-guidelines-to-optimize-your-app-performance-be18533721f9

class StateHoldingClass {
    var counter by mutableStateOf(0)
    var whatAreWeCounting by mutableStateOf("Days without having to write XML.")
}

class RecompositionTrackerViewModel : ViewModel() {

    val stateHoldingClass: StateHoldingClass = StateHoldingClass()

}

@Composable
internal fun CounterRecomposition(viewModel: RecompositionTrackerViewModel) {
    println("Counter Recomposition recomposed")

    Column(
        modifier = Modifier.padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        println("Column recomposed")

        Text("This is a cool column I have")
        CustomButton(count = viewModel.stateHoldingClass.counter) {
            viewModel.stateHoldingClass.counter++
        }
    }
}

@Composable
private fun CustomButton(count: Int, onClick: () -> Unit) {
    println("Custom button recomposed")

    Button(onClick = onClick) {
        println("Custom button Text recomposed")

        Text(text = count.toString())
    }
}
