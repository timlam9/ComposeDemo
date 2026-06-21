package com.example.composedemo.compose.stabilityAnalyzer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composedemo.compose.stabilityAnalyzer.components.MyButton
import com.example.composedemo.compose.stabilityAnalyzer.components.MyText
import com.skydoves.compose.stability.runtime.TraceRecomposition
import org.koin.androidx.compose.koinViewModel

@TraceRecomposition
@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    dataProvider: DataProvider = koinViewModel(),
) {
    val count by dataProvider.myCountState.collectAsStateWithLifecycle()

    val onClick = remember(dataProvider) {
        { dataProvider.increaseCount() }
    }

    println("onClick identity = ${System.identityHashCode(onClick)}")

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        MyText(text = "Hello World: ${count.count}")

        MyButton(
            title = "My Click me",
            onClick = { dataProvider.increaseCount() },
        )

//        MyButton(
//            title = "My Reference Click me",
//            onClick = dataProvider::increaseCount,
//        )
//
//        MyButton(
//            title = "My Stable lambda Click me",
//            onClick = { println("Stable lambda memoized by the compose compiler") },
//        )
//
//        MyButton(
//            title = "Click me",
//            onClick = onClick,
//        )
    }
}
