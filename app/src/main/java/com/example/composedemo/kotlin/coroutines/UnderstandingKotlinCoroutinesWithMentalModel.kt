package com.example.composedemo.kotlin.coroutines

// Source: https://www.lukaslechner.com/understanding-kotlin-coroutines-with-this-mental-model/

// Coroutine =
// It consists of CO and ROUTINE. Every developer is familiar with ordinary routines.
// They are also called subroutines or procedures, but in Java and Kotlin they are known as functions or methods.
// Routines are the basic building blocks of every codebase.

// Co stands for cooperative.
// Instead, they are only executed partially, then get suspended and return back the control flow to other coroutines in the middle of their execution.
// That’s why they are called cooperative routines – they can pass execution back and forth between each other.

// Coroutines can be suspended at every “suspension point”.
// You might wonder what exactly a “suspension point” is. It is basically every point when a coroutine calls a suspend function.


// Coroutines and Threads 🧵
// Threads consume a considerable (1-2 MB per thread) amount of memory and switching between them is quite expensive.
// Coroutines allow you to achieve concurrent behavior without switching threads, which results in more efficient code.
// Therefore, coroutines are often called “lightweight threads”.
// A Coroutine can perform different operations on different threads.
// Coroutines can be seen as abstractions on top of threads.
// Different code blocks within the same coroutine can be executed in different threads.


// It uses a method named Continuation Passing Style.
// The compiler transforms suspend functions into regular functions that receive an additional continuation .
// Depending on the state of the continuation, different code of the suspend function is executed.
// That’s how a coroutine can be split up into separately running code blocks.

// Example:
//suspend fun coroutine(number: Int, delay: Long){
//    println("Coroutine $number starts work")
//    delay(delay)
//    println("Coroutine $number has finished")
//}

// transformed to something like this:
//fun coroutine(number: Int?, delay: Long?, continuation: Continuation<Any?>): Unit {
//    when(continuation.label){
//        0 -> {
//            println("Coroutine $number starts work.")
//            delay(delay)
//        }
//        1 -> {
//            println("Coroutine $number has finished")
//            continuation.resume(Unit)
//        }
//    }
//}


// Coroutines are non-blocking
// delay() is not blocking its host thread because it, depending on the dispatcher, uses mechanisms like handler.postDelayed().
// So it basically just schedules all subsequent operations of the coroutine to a later point in time.

// How delay works:
//override fun delay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
//    val block = Runnable {
//        with(continuation) { resumeUndispatched(Unit) }
//    }
//    handler.postDelayed(block, timeMillis.coerceAtMost(MAX_DELAY))
//    ...
//}