package com.example.composedemo.compose.stabilityAnalyzer.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skydoves.compose.stability.runtime.TraceRecomposition

@TraceRecomposition
@Composable
internal fun MyText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
    )
}