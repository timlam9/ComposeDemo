package com.example.composedemo.compose.stabilityAnalyzer.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skydoves.compose.stability.runtime.TraceRecomposition

@TraceRecomposition
@Composable
internal fun MyButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(title)
    }
}