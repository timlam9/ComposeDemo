package com.example.composedemo.kotlin

interface TestInterface1 {
    fun testFunction() = println("test 1")
}

interface TestInterface2 {
    fun testFunction() = println("test 2")
}

class TestClass : TestInterface1, TestInterface2 {

    override fun testFunction() {
        println("test from TestClass")
    }

    fun testInterface1Function() {
        super<TestInterface1>.testFunction() // Calls the default implementation from TestInterface1
    }

    fun testInterface2Function() {
        super<TestInterface2>.testFunction() // Calls the default implementation from TestInterface2
    }
}

private fun main() {
    val testClass = TestClass()

    testClass.testFunction()
    testClass.testInterface1Function()
    testClass.testInterface2Function()
}