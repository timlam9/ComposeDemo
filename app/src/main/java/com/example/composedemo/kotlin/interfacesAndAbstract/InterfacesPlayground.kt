package com.example.composedemo.kotlin.interfacesAndAbstract


private fun main() {

    val button = Button()
    button.showOff()
    button.setFocus(true)
    button.click()

    println("Java button")
    val javaButton = JavaButton()
    javaButton.showOff()
    javaButton.click()

    println("Rich button")
    val richButton = RichButton()
    richButton.showOff()
    richButton.disable()
    richButton.animate()
    richButton.click()

    println("Themed button")
    val themedButton = ThemedButton()
    themedButton.showOff()
    themedButton.disable()
    themedButton.animate()
}