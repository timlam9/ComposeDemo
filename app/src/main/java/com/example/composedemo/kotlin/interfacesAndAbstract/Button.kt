package com.example.composedemo.kotlin.interfacesAndAbstract

// Interfaces can't contain any state
// All non-abstract classes implementing the interface need to provide an implementation of the
// abstract method
interface Clickable {

    // abstract method
    fun click()

    // default implementation
    fun showOff() = println("I'm clickable!")
}

interface Focusable {

    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")

    fun showOff() = println("I'm focusable!")
}

// Can implement as many interfaces as you want but can extend only one class
class Button : Clickable, Focusable {

    override fun click() = println("I was clicked")

    override fun showOff() {
        super<Focusable>.showOff()
        super<Clickable>.showOff()
    }
}


// Abstract class can't be instantiated
// An abstract class usually contains abstract members that don't have implementations and must be
// overridden in subclasses. Abstract members are always open, so you don't need to use an explicit
// open modifier.
abstract class Animated {

    abstract val animationSpeed: Double

    val keyframes: Int = 20
    open val frames: Int = 60

    abstract fun animate()

    open fun stopAnimation() {}

    fun animateTwice() {
        println("Animating twice")
    }
}

// 1. All classes implementing the interface need to provide an implementation of its abstract
// methods. True or False? --> False (Non-abstract)

// 2. How to override the ("duplicate") showOff method - how to tackle conflicted overloads?
// --> Use super<Clickable>.showOff()
