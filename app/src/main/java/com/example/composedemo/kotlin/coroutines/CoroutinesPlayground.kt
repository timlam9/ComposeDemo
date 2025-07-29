package com.example.composedemo.kotlin.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


fun main234() {
    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch {
        try {
            throw RuntimeException("RuntimeException in coroutine")
        } catch (exception: Exception) {
            println("Handle $exception")
        }
    }

    Thread.sleep(100)
}

fun mainsdf() {
    val topLevelScope = CoroutineScope(Job())

    try {
        topLevelScope.launch {
            launch {
                throw RuntimeException("RuntimeException in nested coroutine")
            }
        }

    } catch (exception: Exception) {
        println("Handle $exception")
    }
    Thread.sleep(100)
}

fun maindfg() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch(coroutineExceptionHandler) {
        launch {
            launch {
                delay(1000)
                println("one")
            }
        }
        launch {
            try {
                delay(500)
                throw RuntimeException("RuntimeException in nested coroutine")
            } catch (e: Exception) {
                println("Hanlde inside")
            }
        }
        launch {
            delay(1200)
            println("two")
        }
    }

    Thread.sleep(2000)
}


// launch{} vs async{}
suspend fun mainghjsdf() {
    val topLevelScope = CoroutineScope(SupervisorJob())

    topLevelScope.async {
        throw RuntimeException("RuntimeException in async coroutine")
    }.await()

    Thread.sleep(100)
}

fun mainsdfsfada() {
    val topLevelScope = CoroutineScope(SupervisorJob())

    val deferredResult = topLevelScope.async {
        throw RuntimeException("RuntimeException in async coroutine")
    }

    topLevelScope.launch {
        try {
            deferredResult.await()
        } catch (exception: Exception) {
            println("Handle $exception in try/catch")
        }
    }

    Thread.sleep(100)
}

fun mainsdfsdf() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }
    val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)

    topLevelScope.launch {
        println("Top level coroutine: ${this@launch.coroutineContext}")
        try {
            topLevelScope.async asyncScope@{
                println("Async coroutine: ${this@asyncScope.coroutineContext}")
                throw RuntimeException("RuntimeException in async coroutine")
            }.await()
        } catch (exception: Exception) {
            println("Handle $exception in try/catch")
        }
    }
    Thread.sleep(100)
}

suspend fun mainfdglkjklfdg() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }
    val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)

    val deferedResult = topLevelScope.async {
        launch childScope@{
            println("Launch coroutine: ${this@childScope.coroutineContext}")
            throw RuntimeException("RuntimeException in nested coroutine")
        }
    }

    deferedResult.await()

    Thread.sleep(100)
}

fun mainkljhdfgjklhlk() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }
    val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)

    topLevelScope.launch {
        launch {
            throw RuntimeException("RuntimeException in nested coroutine")
        }

        launch {
            println("This context: ${this@launch.coroutineContext}")
        }
    }

    Thread.sleep(300)
}

fun main() {

    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch {
        val job1 = launch {
            println("starting Coroutine 1")
        }

        supervisorScope {

            var deferredResult: Deferred<Nothing>? = null

            launch(coroutineExceptionHandler) {
                deferredResult = async {
                    println("starting Coroutine 2 async")
                    throw RuntimeException("Exception in Coroutine 2")
                }
            }

            launch {
                try {
                    delay(200)
                    deferredResult?.await()
                } catch (e: Exception) {
                    println("sdfkjh")
                }
            }


//            val job3 = async {
//                throw RuntimeException("Exception in Coroutine 3")
//                println("starting Coroutine 3")
//            }.await()
        }
    }

    Thread.sleep(500)
}