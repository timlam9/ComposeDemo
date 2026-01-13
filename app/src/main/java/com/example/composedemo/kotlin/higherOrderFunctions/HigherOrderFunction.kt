package com.example.composedemo.kotlin.higherOrderFunctions

fun main() {

    // 1. Have you ever seem the Function0<R>?
    // 2. What is a higher order function?
    // 3. Kotlin function types are regular interfaces (of a FunctionN interface)
    // 4. They look like this but they are generated from the compiler
    // 5. They can be used for every class (Adder, Adder2)

    fun twoAndThree(operation:(Int, Int) -> Int) {
        val result = operation(2,3)
        println(result)
    }

    // synthetic compiler-generated types
//    interface Function1<P1, out R> {
//        operator fun invoke(p1: P1): R
//    }

//    fun processTheAnswer(func: (Int)-> Int) {
//        println(func.invoke(42))
//    }
//
//    fun processTheAnswer(func: Function1<Int, Int>) {
//        println(func.invoke(67))
//    }

    class Adder: (Int, Int) -> Int {
        override fun invoke(p1: Int, p2: Int): Int {
            return p1 + p2
        }
    }

    class Adder2: (Int, Int, Int, Int) -> Int {
        override fun invoke(p1: Int, p2: Int, p3: Int, p4: Int): Int {
            return p1 + p2 + p3 + p4
        }
    }

//    processTheAnswer { number -> number + 1 }
}