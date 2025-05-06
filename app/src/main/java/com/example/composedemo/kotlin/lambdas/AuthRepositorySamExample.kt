package com.example.composedemo.kotlin.lambdas

interface AuthRepositor {
    fun register(username: String, password: String)
}

interface RegisterUserUseCas {
    suspend operator fun invoke(username: String, password: String)
}

class RegisterUserUseCaseImpl(val authRepositor: AuthRepositor): RegisterUserUseCas {
    override suspend operator fun invoke(username: String, password: String) {
        authRepositor.register(username, password)
    }
}


//typealias Username = String
//typealias Password = String
//
//interface AuthRepository {
//    fun register(username: Username, password: Password)
//}
//
//fun interface RegisterUserUseCase: (Username, Password) -> Unit
//
//fun provideUC(repository: AuthRepository): RegisterUserUseCase = RegisterUserUseCase(repository::register)

//private fun main() {
//
//    val repository = object : AuthRepository {
//        override fun register(username: Username, password: Password) {
//            println("Registering $username with $password")
//        }
//
//    }
//    val uc = provideUC(repository)
//    uc("George", "sifou")
//}