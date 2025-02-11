package com.example.composedemo.kotlin.interfacesAndAbstract

open class RichButton : Clickable {

    // redundant final
    final fun disable() {
        println("I was disabled")
    }

    open fun animate() {
        println("I was animated")
    }

    // final not redundant - you can use it to make the overridden method non-open
    override fun click() {
        println("I was clicked")
    }
}

class ThemedButton : RichButton() {

    override fun animate() {
        super.animate()
    }

    override fun click() = println("I was clicked")

    override fun showOff() {
        super.showOff()
    }
}



// 4. All functions in Kotlin are by default final (thus final fun is redundant) Why do we need it then?
// 5. You can use open to make a class inheritable (and not final)
// 6. Overridden functions are by default open. You can use final to make an overridden function
// non-overridable (and not open)
// 7. breakProject is redundant because you can call the method without overriding it
// 8. You cannot use final inside interfaces
