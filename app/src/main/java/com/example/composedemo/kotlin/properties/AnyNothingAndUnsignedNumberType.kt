package com.example.composedemo.kotlin.properties

// Unsigned number types in Kotlin
// 1. UByte  | 8 bit  | 0 - 255
// 2. UShort | 16 bit | 0 - 65535
// 3. UInt   | 32 bit | 0 - 2 ^ 32 - 1
// 4. ULong  | 64 bit | 0 - 2 ^ 64 - 1

// Byte -> -128..127 / UByte -> 0..255
// With unsigned number types you can store the larger non-negative numbers in the same amount of memory
// UInt it is a inline Int class

// Object is the root hierarchy in Java (reference types only, not primitive types)
// Any is supertype of all non-nullable types in Kotlin
// toString, equals and hashCode functions are inherited by Any

// Unit type in Kotlin is like void in Java
// Nothing type in Kotlin is a type that has no value. It is used to mark functions that never return (do not complete successfully)
// That's why void is replaced with Unit, so there is no confusion between them

// Nothing example:
internal fun fail(): Nothing {
    throw IllegalStateException("Something went wrong")
}

fun returnLengthOfNullableString(nullableString: String? = null): Int {
    val length: Int = nullableString?.length ?: fail()

    return length
}
