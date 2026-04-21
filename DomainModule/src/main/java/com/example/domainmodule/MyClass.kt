package com.example.domainmodule

import java.security.Provider
import java.util.ServiceLoader

class MyClass {

    inline fun <reified T> loadService(): ServiceLoader<T > {
        return ServiceLoader.load(T::class.java)
    }

    fun test() {
        val service = ServiceLoader.load(Provider.Service::class.java)
        val service2 = loadService<Provider.Service>()
        listOf<String>().map {  }

    }
}