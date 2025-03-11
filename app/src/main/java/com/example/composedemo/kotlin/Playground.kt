package com.example.composedemo.kotlin

import com.example.composedemo.kotlin.innerAndDataClasses.Customer

private fun main() {
    println(
        Customer("George", 12345)
                == Customer("George", 12345)
    )

    val hashset = hashSetOf(Customer("George", 12345))

    println(hashset.contains(Customer("George", 12345)))

    println(Customer("George", 12345).hashCode()) // 1585817626
    println(Customer("George", 12345).toString()) // Customer(name=George, postalCode=12345)//Customer(name=George, postalCode=12345)
}
