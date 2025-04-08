package com.example.composedemo.kotlin.lambdas

private fun main() {
    DelegatingCollection(mutableListOf(1, 2, 3))

    Person.createObjectWith("bla", "bla").print()
    Person.createObject("bla bla").print()

    val computation = object : Runnable {
        override fun run() {

            println("What is $this")
        }
    }

    postponeComputation(
        delay = 1000,
        computation = computation,
    )
    computation.run()


    handleComputation("A new object is created in every invocation and stores the values of the captured variables in that object.")
}

private class DelegatingCollection<T>(
    innerList: Collection<T> = mutableListOf()
) : Collection<T> by innerList


private class Person(
    val firstName: String,
    val lastName: String,
) {

    companion object : TestFactory<Person> {

        override fun createObject(test: String): Person {
            return Person(test, test)
        }
    }
}

private interface TestFactory<T> {

    fun createObject(test: String): T
}

private fun Person.Companion.createObjectWith(name: String, lastName: String): Person {
    return Person(name, lastName)
}

private fun Person.print() {
    println("First name: $firstName, last name: $lastName")
}


internal fun postponeComputation(delay: Int, computation: Runnable) {
    // Don't have a this reference
}

private fun handleComputation(id: String) {
    postponeComputation(1000) {
        println("Computing for id: $id")
    }
}


private fun interface IntCondition {

    fun check(i: Int): Boolean

    fun checkString(s: String): Boolean = check(s.toInt())

    fun checkChar(c: Char): Boolean = check(c.digitToInt())
}

private fun checkCondition(i: Int, condition: IntCondition): Boolean {
    return condition.check(i)
}


// ---> SAM interfaces and Lambdas <---

// 1. Functional interfaces OR Single Abstract Method (SAM) interfaces --> Interfaces with exactly one abstract method
// 2. Kotlin can replace functional interfaces with lambda functions (like Runnable and Callable)
// 3. Different between those two is that for the functional interface a new object is created in every invocation
//    but with the lambda only one anonymous class is created and reused between calls, except if the lambda captures
//    variables from the surrounding scope.
// 4. Lambdas as anonymous class do not have object reference (this) - Why lambdas do not have the 'this' reference?
//    -> Compiler point of view lambdas is a block of code and not an object so they cannot have a reference.
//    But the can have references of the surrounding scope.
// 5. In Kotlin you can create functional interfaces like this: fun interface myFunInterface
