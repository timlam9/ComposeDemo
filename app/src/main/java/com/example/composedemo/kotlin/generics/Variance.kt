package com.example.composedemo.kotlin.generics

import android.content.pm.ResolveInfo

internal class Variance {

    // Class - Type
    // A class is a blueprint — something you declare in code.
    // A type is what the compiler uses to reason about values.
    // Each Kotlin class can be used to construct at least two types: Sting, String?

    // List isn't a type is a class. List<String> is a type. String is a class
    // Int class is subclass of Number -> Int type is subtype of Number type. Int is subtype of Int?

    fun nullableSubtypeExample() {
        val intNumber: Int = 34
        val intNumberNullable: Int? = intNumber // ✅ Int is subtype of Int?
    }

    // Variance describes how types with same base type and different type arguments relate to each other.
    // For example, List<String> and List<Any>.

    sealed class Fruit {
        abstract val weight: Int
    }

    data class Apple(
        override val weight: Int,
        val color: String,
    ) : Fruit()

    data class Orange(
        override val weight: Int,
        val juicy: Boolean,
    ) : Fruit()

    fun fruitsExample() {
        val apples: MutableList<Apple> = mutableListOf(Apple(weight = 100, color = "red"))
//        val fruits: MutableList<Fruit> = apples  // ❌ if this was allowed
//
//        fruits.add(
//            Orange(
//                weight = 200,
//                juicy = true
//            )
//        ) // Now the original apples list contains an Orange -> 💥 type safety broken.
        val apple: Apple = apples.first() // 💥 runtime crash
    }


    // Covariance
    // Covariant -> List<Apple> is a subtype of List<Fruit> because Apple is a subtype of Fruit.
    fun covarianceExample() {
        val fruits: List<Fruit> = listOf(Apple(weight = 100, color = "red"), Orange(weight = 200, juicy = true))
        val apples: List<Apple> = fruits.filterIsInstance<Apple>()
        println("Apples: $apples")
    }


    open class Animal {

        fun feed() {
            println("Animal is feeding")
        }
    }

    class Herd<T : Animal> {

        val size: Int get() = TODO()

        operator fun get(index: Int): T = TODO()
    }

    fun feedAll(animals: Herd<Animal>) {
        for (i in 0..animals.size) {
            animals[i].feed()
        }
    }

    class Cat : Animal() {

        fun meow() {
            println("Cat is meowing")
        }
    }

    fun takeCareOfCats(cats: Herd<Cat>) {
        for (i in 0 until cats.size) {
            cats[i].meow()
        }

        //feedAll(cats)
        // Error: Type mismatch: inferred type is Herd<Cat> but Herd<Animal> was expected
        // Solution: use out keyword in the Herd class declaration

        // out means that the subtyping is preserved and T can be used only in out positions.

        // Kotlin Example: List<out T>
        // That's why MutableList is invariant, because T is used in both in and out positions.

        // Constructor parameters are not in and out positions because constructor is not a method that can be called later
        // so there is no danger for type safety.
//        Herd<out T: Animal>(vararg animals: T)
        // But you cannot add a var parameter as it will create a setter and T will be called in in positions.
//        Herd<out T: Animal>(var leadAnimal: T) ❌Not valid
//        Herd<out T: Animal>(private var leadAnimal: T) ✅ Valid as parameters of private methods are in neither in or out positions.
    }


    // Contravariance
    // Contravariant -> Comparator<Any> is a subtype of Comparator<String> because Any is a supertype of String.
    fun contravarianceExample() {
        val weightComparator = Comparator<Fruit> { o1, o2 -> o1.weight - o2.weight }
        val fruits: List<Fruit> =
            listOf(Apple(weight = 100, color = "red"), Orange(weight = 200, juicy = true))
        val apples: List<Apple> = listOf(
            Apple(weight = 50, color = "green"),
            Apple(weight = 75, color = "yellow"),
            Apple(weight = 100, color = "green"),
        )
        println("Fruits: ${fruits.sortedWith(weightComparator)}")
        println("Apples: ${apples.sortedWith(weightComparator)}")
        // You can use weightComparator for any collection of objects that are a subtype of Fruit (Apple, Orange, etc.)

        // in means that the subtyping is reversed and T can be used only in in positions.
    }

    fun contravarianceExample2() {
        val list: List<String> = listOf("a", "b")
        val comparator: Comparator<String> = Comparator { o1, o2 -> o1.compareTo(o2) }
        println("Comparator: ${comparator.compare("a", "b")}")

        // Comparator is contravariant (Comparator<in T>)
        val anyComparator: Comparator<Any> =
            Comparator { a, b -> a.toString().length - b.toString().length }

        // call site in!
        // Because of contravariance, we can assign Comparator<Any> to Comparator<String>
//        val stringComparator: Comparator<in String> = anyComparator
//        val sorted = list.sortedWith(stringComparator)

        val sorted: List<String> = list.sortedWith(anyComparator) // ✅ works
        println("Sorted list with Comparator<Any>: $sorted")
    }


    // Class or interface can be covariant on one type parameter and contravariant on another.
    // For example, Function interface.
    fun functionExample() {
//        Function1<Int, String>()

        fun Animal.getIndex(): Int = TODO()
        fun enumerateCats(f: (Cat) -> Number) {}

        val lambda: (Cat) -> Number = { it.getIndex() }
        val lambda2: (Animal) -> Number = Animal::getIndex

        enumerateCats(f = lambda2)
        // so enumerateCats here accepts a lambda with type (Animal) -> Number instead of (Cat) -> Number
        // so (Animal) -> Number is a subtype of (Cat) -> Number because Animal is a supertype of Cat.
        // this works because of the Function1<in A, out B> type rules
        // parameter type (A) is contravariant and return type B is covariant.

        // Animal is superType of Cat and Int is subtype of Number

        // more detailed example to understand contravariance
        val f: (Cat) -> Number = { cat: Cat ->
            // treat cat as Animal (safe, since Cat is an Animal)
            val animal: Animal = cat

            // call function that returns Int
            val result: Int = animal.getIndex()

            // Int is a Number → OK
            result
        }
    }
}


