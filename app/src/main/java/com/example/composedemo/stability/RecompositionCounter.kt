package com.example.composedemo.stability

import android.util.Log
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

private class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}

private data class CounterState(
    val count1: Int = 0,
    val count2: Int = 0,
    val manualCount: Int = 0,
    val unstableClass: UnstableClass = UnstableClass(),
)

@Immutable
private data class UnstableClass(
    var unstableCount: Int = 0
)

@Composable
internal fun RecompositionCounter() {
    var counterState by remember { mutableStateOf(CounterState()) }

    Column {
        MyText(value = counterState.count1.toString())
        MyText(value = counterState.count2.toString())
        MyBox(value = counterState.unstableClass)
        Button(
            interactionSource = NoRippleInteractionSource(),
            onClick = {
                counterState = counterState.copy(manualCount = counterState.manualCount + 1)
            }
        ) {
            Text("Increase Manual Count: ${counterState.manualCount}")
        }
    }
}

@Composable
private fun MyText(modifier: Modifier = Modifier, value: String) {
    Box(modifier = modifier.padding(20.dp)) {
        Log.d("RECOMPOSITION", "MyText recomposed with value: $value")
        Text("My text: $value")
    }
}

@Composable
private fun MyBox(modifier: Modifier = Modifier, value: UnstableClass) {
    Box(modifier = modifier.padding(20.dp)) {
        Log.d("RECOMPOSITION", "MyText recomposed with value: ${value.unstableCount}")
        Text("My text: ${value.unstableCount}")
    }
}
