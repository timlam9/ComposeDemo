package com.example.composedemo.kotlin.generics

interface DeclarationSiteVariance<out T : Number> {

    fun get(): T
}

fun useSiteVariance() {
    // Also called (out) projection
    val myList: MutableList<out Number> = mutableListOf(1, 2.5, 3L)
//    myList.add(4)
}

fun starProjection() {
    val myList: MutableList<*> = mutableListOf(1, 2.5, 3L)
//    myList.add(4)

    val anyList: MutableList<Any?> = mutableListOf(1, 2.5, 3L)
    val starProjectionList: MutableList<*> = mutableListOf(1, 2.5, 3L)

    anyList != starProjectionList

    anyList.add("string")

//    starProjectionList.add("string")
//    starProjectionList.add(4)
}
