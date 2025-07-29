package com.example.composedemo.kotlin.platformTypes

private fun main() {
    yellAt(Person(null))
}

private fun yellAt(person: Person) {
    val personName = person.name
    println(personName?.uppercase() + "!!!")
}
