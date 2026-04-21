package com.example.composedemo.kotlin.generics

internal class Generics {

    // Generic property declaration
    val <T> List<T>.penultimate: T
        get() = this[size - 2]

    // Error: Type parameter of a property must be used in its receiver type or context parameters
//    val <T> x: T = TODO()

    // Generic Interface

    interface ListExample<T> {
        operator fun get(index: Int): T
    }

    // Generic function declaration
    fun <T> myGenericFunction(genericParam: T): T {

        val df: ListExample<String> = object : ListExample<String> {
            override fun get(index: Int): String = ""
        }
        df[4]

        return genericParam
    }



    // Type parameter -> <T>
    // It is used in receivers and return types
    fun <reT> List<T>.listGenericFunction(param: T): T {
        return first()
    }

    // Type parameters constraints
    fun <T : Number> List<T>.mySum(): T {
        return TODO()
    }

    // Multiple type constraints
    fun <T> ensureTrailingPeriod(seq: T) where T : CharSequence, T : Appendable {
        if (!seq.endsWith('.')) {
            seq.append('.')
        }
    }

    // Non-nullable types
    class Processor<T : Any> {
        fun process(value: T) {
            value.hashCode()
        }
    }



    // T & Any
    class ProcessorWithFunParamNotNull<T> {
        fun process(value: T & Any) {
            value.hashCode()
        }
    }
//    class KBox<T> : JBox<T> {
//        override fun put(t: T & Any) {}
//        override fun putIfNotNull(t: T) {}
//    }

    // Reified + type erasure at run time
    inline fun <reified T> reifiedDemo() {
        val list1: List<String> = listOf("a","b")
        val list2: List<Int> = listOf(1,2,3)
        // Both lists with some objects at run time


    }

    fun readNumbersOrWords(): List<Any> {
        val input = readln()
        val words = input.split(",")
        val numbers = words.mapNotNull { it.toIntOrNull() }

        return numbers.ifEmpty { words }
    }



    fun printList(l: List<Any>) {
        when(l) {
            is List<String> -> println("Strings: $l")
            is List<Int> -> println("Integers: $l")
        }
    }

    fun erasedTypesDemo() {
        val list = readNumbersOrWords()
        printList(list)
    }

    fun genericsDemo() {
        // 1. Type arguments
        val fun1 = myGenericFunction<String>(genericParam = "String argument")
        val fun2 = listOf<Int>(1, 2, 3).listGenericFunction(param = 5)

        println("Fun 1: $fun1")
        println("Fun 2: $fun2")

        // 2. Type constraints
        val helloWorld = StringBuilder("Hello World")
        ensureTrailingPeriod(helloWorld)
        println("Hello World: $helloWorld")


    }
}
