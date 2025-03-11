package com.example.composedemo.kotlin.innerAndDataClasses

class Customer(
    val name: String,
    val postalCode: Int,
) {
    override fun hashCode(): Int {
        return name.hashCode() * 31 + postalCode
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Customer)
            return false

        return name == other.name && postalCode == other.postalCode
    }

    override fun toString(): String {
        return "Customer(name=$name, postalCode=$postalCode)"
    }
}