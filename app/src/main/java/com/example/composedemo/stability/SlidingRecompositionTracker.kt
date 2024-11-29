package com.example.composedemo.stability

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

class SlidingRecompositionTrackerViewModel : ViewModel() {

    private val list: PersistentList<String>
        get() {
            val list = mutableListOf(
                "Sifou George",
                "MC John",
                "Giant Ime",
            )
            repeat(3) { list.addAll(list) }

            return list.toPersistentList()
        }


    var venueName by mutableStateOf("Java hall")
    var concertPerformers = mutableStateOf(list)
}

@Composable
internal fun SlidingScreen(
    viewModel: SlidingRecompositionTrackerViewModel,
) {
    println("Sliding Screen recomposed")

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        SlidingComposable(scrollPosition = scrollState.value)

        ConcertPerformers(
            modifier = Modifier.weight(1f),
            scrollState = scrollState,
            venueName = viewModel.venueName,
            performers = viewModel.concertPerformers.value
        )
    }
}

@Composable
private fun SlidingComposable(scrollPosition: Int) {
    println("Sliding Composable recomposed")

    val scrollPositionInDp = with(LocalDensity.current) { scrollPosition.toDp() }

    Card(
        modifier = Modifier.offset(scrollPositionInDp),
        colors = CardDefaults.cardColors(containerColor = Color.Cyan)
    ) {
        println("Sliding Composable Text recomposed")

        Text(text = "Hello I slide out")
    }
}

@Composable
private fun ConcertPerformers(
    scrollState: ScrollState,
    venueName: String,
    performers: PersistentList<String>,
    modifier: Modifier = Modifier,
) {
    println("Concert Performer recomposed")

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.background(color = Color.LightGray),
            text = "The following performers are performing at $venueName tonight:"
        )

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            for (item in performers) {
                PerformerItem(performer = item)
            }
        }
    }
}

@Composable
private fun PerformerItem(performer: String) {
    println("Performer Item recomposed")

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = performer
        )
    }
}
