package com.example.composedemo.kotlin.visibilityAndInnerClasses

import java.io.Serializable

// Inner vs Nested class
interface State: Serializable

interface View {

    fun getCurrentState(): State

    fun restoreState(state: State) {}
}

class StatefulButton: View {

    override fun getCurrentState(): State = ButtonState()

    override fun restoreState(state: State) {
        println("Restoring Button state")
    }

    // has not reference of the outer class
    class ButtonState: State
}

class Outer {
    inner class Inner {

        fun getOuterReference(): Outer = this@Outer
    }
}

// 10. What's the difference between inner and nested classes?
// --> Inner classes has a reference to the outer class and nested classes don't

// 4.1.5 Sealed classes (page: 120/550)
