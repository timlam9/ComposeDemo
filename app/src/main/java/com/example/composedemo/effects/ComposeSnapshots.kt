package com.example.composedemo.effects

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot

private fun main() {
    composeSnapshots()
}

internal fun composeSnapshots() {
    var status by mutableStateOf("")
    status = "Working at Moro"

    val firstSnapshot = Snapshot.takeMutableSnapshot()

    firstSnapshot.enter {
        status = "Working at WeCraft"

        val secondSnapshot = Snapshot.takeMutableSnapshot()

        secondSnapshot.enter {
            status = "OAED"
        }

        println("COMPOSE_SNAPSHOT, status: $status") // wecraft

        secondSnapshot.apply()
        println("COMPOSE_SNAPSHOT, status: $status") // oaed
    }
    println("COMPOSE_SNAPSHOT, status: $status") // moro

    firstSnapshot.apply()
    println("COMPOSE_SNAPSHOT, status: $status") // oaed
}
