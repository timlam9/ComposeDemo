package com.example.composedemo.kotlin.generics

import org.junit.Test

internal class VarianceTest {


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


    open class Animal {

        fun feed() {
            println("Animal is feeding")
        }
    }

    class Herd<out T : Animal> {

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


    @Test
    fun variance1() {
        // Variance
        val stringType: String = "ok"
        val anyType: Any = "ok"

        val okd: List<String>
        val ok: List<Any>


//        val apples = mutableListOf(Apple(weight = 100, color = "red"))
//        val fruits: MutableList<Fruit> = apples
//
//        val spp = apples.first()
//
//        fruits.add(Orange(weight = 200, juicy = true))
//
//        val fruits: List<Fruit> = listOf(Apple(weight = 100, color = "red"), Orange(weight = 200, juicy = true))
//        val apples: List<Apple> = fruits.filterIsInstance<Apple>()

        fun takeCareOfCats(cats: Herd<Cat>) {
            for (i in 0 until cats.size) {
                cats[i].meow()
            }

            feedAll(animals = cats)
//
//            Herd<Animal>
//            Herd<Cat>
        }
    }

    fun contravariant() {
        val weightComparator = Comparator<Fruit> { o1, o2 -> o1.weight - o2.weight }

        val fruits: List<Fruit> = listOf(Apple(weight = 100, color = "red"), Orange(weight = 200, juicy = true))

        val apples: List<Apple> = listOf(
            Apple(weight = 50, color = "green"),
            Apple(weight = 75, color = "yellow"),
            Apple(weight = 100, color = "green"),
        )

        println("Fruits: ${fruits.sortedWith(weightComparator)}")
        println("Apples: ${apples.sortedWith(weightComparator)}")

        // out means that the subtyping is preserved and T can be used only in out positions.
        // in means that the subtyping is reversed and T can be used only in in positions.
    }
}
