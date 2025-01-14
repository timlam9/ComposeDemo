package com.example.composedemo.compose.stability

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// https://x.com/QamarSafadi/status/1857526757445837076
// https://www.linkedin.com/feed/update/urn:li:activity:7263290684701360129/

@Composable
fun ListRecompositionTrackerScreen() {
    println("List Recomposition tracker screen recomposed")

    val inputText = remember { mutableStateOf("") }
    val items = remember { mutableStateListOf<String>() }

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            label = { Text("Enter item") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                if (inputText.value.isNotBlank()) {
                    items.add(inputText.value)
                    inputText.value = ""
                }
            },
            modifier = Modifier,
        ) {
            println("Button Text recomposed")
            Text("Add item")
        }
        Spacer(modifier = Modifier.height(20.dp))
        MyList(items)
    }
}

@Composable
fun MyList(items: SnapshotStateList<String>) {
    println("My List recomposed")

    LazyColumn {
        items(items) {
            MyItem(it)
        }
    }
}

@Composable
private fun MyItem(item: String) {
    println("My Item recomposed")

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = item,
        )
    }
}
