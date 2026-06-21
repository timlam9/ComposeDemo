package com.example.composedemo.compose.stabilityAnalyzer

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


internal class DataProvider : ViewModel() {
    private val count: MutableStateFlow<Int> = MutableStateFlow(0)

    val myCountState = count.map {
        MyCount(
            count = it,
        )
    }.stateIn(
        initialValue = MyCount(count = 0),
        started = SharingStarted.WhileSubscribed(5000),
        scope = MainScope(),
    )

    fun increaseCount() {
        count.update { it + 1 }
    }

    fun decreaseCount() {
        count.update { it - 1 }
    }
}