package com.example.composedemo.kotlin.properties

// Agenda:
// ----------------------------------------------------------------------------
// 1. field vs property [parameter vs argument]
// 2. (a) custom getter/setter (b) vs member function
// 3. Backing properties
// ----------------------------------------------------------------------------


// Documentation:
// ----------------------------------------------------------------------------
// Fields:
// Definition: Fields are variables that are declared directly within a class.
// They hold the data associated with an object of that class.

// Properties:
// Definition: Properties are a higher-level concept that encapsulates both
// the data (like a field) and the logic for accessing and modifying that data.

// Java vs Kotlin:
// While Java doesn't have properties as a built-in language feature, the
// concept of properties is still important in Java development. You achieve
// similar functionality by using private fields and public getters and setters.
// Kotlin, on the other hand, makes properties a core part of the language,
// making them much easier and more concise to use.
// ----------------------------------------------------------------------------

class Oaed {

    // property
    val status: String = "Opened"

    // mutable property
    var numberOfUnemployed: Int = 6
}

class Person(
    val name: String,
    var isUnemployed: Boolean,
)

// 2.2.2
class MyRectangle(
    val width: Int,
    val height: Int,
) {
    // custom getter: It's the same as a member function
    val isRectSquare: Boolean
        get() = width == height

    // same as a custom getter property (performance and implementation)
    // In general use property if you describe characteristic of a class
    // and member function if you describe behavior of a class
    fun isSquare(): Boolean = width == height
}

// 4.2.4
class PersonWithoutBackingFields(var birthYear: Int) {
    var ageIn2050
        get() = 2050 - birthYear
        set(value) {
            birthYear = 2050 - value
        }
}

class UserWithBackingFields(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println(
                """
                Address was changed for $name:
                "$field" -> "$value".
                """.trimIndent()
            )
            field = value
        }
        get() = field
}