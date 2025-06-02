package com.example.composedemo.kotlin.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

// Exception handling in Kotlin:

fun kotlinExceptionHandling() {
    try {
        // some code
        throw RuntimeException("RuntimeException in 'some code'")
    } catch (exception: Exception) {
        println("Handle $exception")
    }
}

// Output:
// Handle java.lang.RuntimeException: RuntimeException in 'some code'


// If an exception is thrown inside a regular function, this exception is “re-trown” by the function.
// This means that we are able to use a try-catch clause to handle the exception on the call site:
fun main1() {
    try {
        functionThatThrows()
    } catch (exception: Exception) {
        println("Handle $exception")
    }
}

fun functionThatThrows() {
    // some code
    throw RuntimeException("RuntimeException in regular function")
}

// Output
// Handle java.lang.RuntimeException: RuntimeException in regular function


// Try-Catch in Coroutines
fun main2() {
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

// Output
// Handle java.lang.RuntimeException: RuntimeException in coroutine


// Exception isn't handled in nested coroutines and app crashes
// Check uncaught_exception_propagation.webp
fun main3() {
    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch {
        try {
            launch {
                throw RuntimeException("RuntimeException in nested coroutine")
            }
        } catch (exception: Exception) {
            println("Handle $exception")
        }
    }

    Thread.sleep(100)
}

// Output
// Exception in thread "main" java.lang.RuntimeException: RuntimeException in nested coroutine

// Why is this happening?
// Well, a Coroutine that doesn’t catch an exception by itself with a try-catch clause, “completes exceptionally” or in simpler terms, it “fails”.
// In the example above, the Coroutine started with the inner launch doesn’t catch the RuntimeException by itself and so it fails.

// An uncaught exception, instead of being re-thrown, is “propagated up the job hierarchy”.
// This exception propagation leads to the failure of the parent Job, which in turn leads to the cancellation of all the Jobs of its children.
// And this is due to Structured Concurrency!

// 💥 Key Point 1
// If a Coroutine doesn’t handle exceptions by itself with a try-catch clause, the exception isn’t re-thrown and can’t, therefore, be handled by an outer try-catch clause.
// Instead, the exception is “propagated up the job hierarchy” and can be handled by an installed CoroutineExceptionHandler.
// If none is installed, the uncaught exception handler of the thread is invoked.


// Coroutine Exception Handler
fun main4() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

// You can add it here:
//    val topLevelScope = CoroutineScope(Job() + coroutineExceptionHandler)
    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch(coroutineExceptionHandler) {
//        launch(coroutineExceptionHandler) {   <----------- This is wrong! Cannot add it to child coroutines!
        launch {
            throw RuntimeException("RuntimeException in nested coroutine")
        }
    }

    Thread.sleep(100)
}

// Output:
// Handle java.lang.RuntimeException: RuntimeException in nested coroutine in CoroutineExceptionHandler

// 💥 Key Point 2
// In order for a CoroutineExceptionHandler to have an effect, it must be installed either in the CoroutineScope or in a top-level coroutine.


// try-catch VS CoroutineExceptionHandler
// When to use which? (Answer from official documentation: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-exception-handler/)
// “CoroutineExceptionHandler is a last-resort mechanism for global “catch all” behavior.
// You cannot recover from the exception in the CoroutineExceptionHandler.
// The coroutine had already completed with the corresponding exception when the handler is called.
// Normally, the handler is used to log the exception, show some kind of error message, terminate, and/or restart the application.
//
// If you need to handle exception in a specific part of the code, it is recommended to use try/catch around the corresponding code inside your coroutine.
// This way you can prevent completion of the coroutine with the exception (exception is now caught), retry the operation, and/or take other arbitrary actions:”

// 💥 Key Point 3
// Use try/catch if you want to retry the operation or do other actions before the Coroutine completes.
// Keep in mind that by catching the exception directly in the Coroutine, it isn’t propagated up the job hierarchy and you aren’t making use of the cancellation functionality of Structured Concurrency.
// Use the CoroutineExceptionHandler for logic that should happen after the coroutine already completed.


// launch{} vs async{}
fun main5() {
    val topLevelScope = CoroutineScope(SupervisorJob())

    topLevelScope.async {
        throw RuntimeException("RuntimeException in async coroutine")
    }

    Thread.sleep(100)
}

// No output

fun main6() {
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

// Output:
// Handle java.lang.RuntimeException: RuntimeException in async coroutine in try/catch

fun main7() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }
    val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)

    topLevelScope.launch {
        println("Top level coroutine: ${this@launch.coroutineContext}")
        launch childScope@ {
            println("Launch coroutine: ${this@childScope.coroutineContext}")
        }
        async asyncScope@ {
            println("Async coroutine: ${this@asyncScope.coroutineContext}")
            throw RuntimeException("RuntimeException in async coroutine")
        }
    }
    Thread.sleep(100)
}

// Output
// Handle java.lang.RuntimeException: RuntimeException in async coroutine in CoroutineExceptionHandler

// 💥 Key point 4
// Uncaught exceptions in both launch and async Coroutines are immediately propagated up the job hierarchy.
// However, if the top-level Coroutine was started with launch, the exception is handled by a CoroutineExceptionHandler or passed to the thread’s uncaught exception handler.
// On the other hand, if the top-level Coroutine was started with async, the exception is encapsulated in the Deferred return type and re-thrown when .await() is called on it.

// jajajajajajajajajajajajajajajajaja
//fun main() {
//    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
//        println("Handle $exception in CoroutineExceptionHandler")
//    }
//    val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)
//
//    topLevelScope.launch {
//        try {
//            async asyncScope@ {
//                throw RuntimeException("RuntimeException in async coroutine")
//            }.await()
//        } catch (exception: Exception) {
//            println("Handle $exception in try/catch")
//        }
//    }
//    Thread.sleep(100)
//}


// Exception handling properties of coroutineScope{}

fun main8() {
    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch {
        try {
            coroutineScope {
                launch {
                    throw RuntimeException("RuntimeException in nested coroutine")
                }
            }
        } catch (exception: Exception) {
            println("Handle $exception in try/catch")
        }
    }

    Thread.sleep(100)
}

// Output
// Handle java.lang.RuntimeException: RuntimeException in nested coroutine in try/catch

// coroutineScope{} is mainly used in suspend functions to achieve “parallel decomposition”.
// These suspend functions will re-throw exceptions of their failed coroutines and so we can set up our exception handling logic accordingly.
//
// 💥 Key Point 5
// The scoping function coroutineScope{} re-throws exceptions of its failed child coroutines instead of propagating them up the job hierarchy,
// which allows us to handle exceptions of failed coroutines with try-catch.su


// Exception Handling properties of supervisorScope{}

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
            val job2 = launch(coroutineExceptionHandler) {
                println("starting Coroutine 2")
                throw RuntimeException("Exception in Coroutine 2")
            }

            val job3 = launch {
                println("starting Coroutine 3")
            }
        }
    }

    Thread.sleep(100)
}

// Output
// starting Coroutine 1
// starting Coroutine 2
// Handle java.lang.RuntimeException: Exception in Coroutine 2 in CoroutineExceptionHandler
// starting Coroutine 3

// The fact that coroutines that are started directly in supervisorScope are top-level Coroutines also means that async Coroutines now encapsulate their exceptions in their Deferred objects…

// ... other code is identical to example above
//supervisorScope {
//    val job2 = async {
//        println("starting Coroutine 2")
//        throw RuntimeException("Exception in Coroutine 2")
//    }
// ...

// Output:
// starting Coroutine 1
// starting Coroutine 2
// starting Coroutine 3

// 💥 Key Point 6
// The scoping function supervisorScope{} installs a new independent sub-scope in the job hierarchy with a SupervisorJob as the scope’s job.
// This new scope does not propagate its exceptions “up the job hierarchy” so it has to handle its exceptions on its own.
// Coroutines that are started directly from the supervisorScope are top-level coroutines.
// Top-level coroutines behave differently than child coroutines when they are started with launch() or async() and furthermore it is possible to install CoroutineExceptionHandlers in them.