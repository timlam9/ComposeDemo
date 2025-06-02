package com.example.composedemo.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// Sources:
// 1. https://www.lukaslechner.com/coroutines-exception-handling-cheat-sheet/
// 2. https://www.lukaslechner.com/why-exception-handling-with-kotlin-coroutines-is-so-hard-and-how-to-successfully-master-it/
// 3. https://www.lukaslechner.com/7-common-mistakes-you-might-be-making-when-using-kotlin-coroutines/

val coroutine = CoroutineScope(context = Job())

// 1. Exceptions can be handled directly in a Coroutine with a try-catch block. This way, the Coroutine doesn’t complete exceptionally.
fun handleExceptionDirectlyWithTryCatch() {
    coroutine.launch {
        try {
            throw Exception()
        } catch (e: Exception) {
            println("Handled $e")
        }
    }
}

// 2. If an exception is thrown in a Coroutine and not handled directly within the Coroutine with a try-catch block, the Coroutine completes exceptionally.
// 3. As a result, the exception is propagated up the job hierarchy until it reaches either the RootScope or a SupervisorJob.
// While the exception travels upwards, parent coroutines fail too and sibling coroutines get canceled.
// 4. When the root coroutine was started with launch{}, the exception will be passed to an installed CoroutineExceptionHandler.
// When the root coroutine was started with async{}, the exception is encapsulated in the Deferred object.

// 5. The scoping function coroutineScope{} re-throws uncaught exceptions of its child Coroutines and so we can handle them with try/catch.
fun handleExceptionOfCoroutineScopeWithTryCatch() {
    println("Start")

    coroutine.launch {
        println("Coroutine: $coroutineContext")
        try {
            coroutineScope {
                println("Coroutine: $coroutineContext")

                launch {
                    println("Coroutine: $coroutineContext")

                    throw Exception()
                }
            }
        } catch (e: Exception) {
            println("Handled $e")
        }
    }

    Thread.sleep(1000)
}

// 6. Keep in mind:
// When your Coroutine is not completing exceptionally, parent and sibling coroutines won’t be canceled.
// Suspend functions can throw a CancellationException at any point and by catching them, the Coroutine will keep on running.

fun main() {
    // handleExceptionDirectlyWithTryCatch()
    handleExceptionOfCoroutineScopeWithTryCatch()
}