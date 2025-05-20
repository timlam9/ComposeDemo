package com.example.composedemo.kotlin.collections

fun List<String>.filterCollection() = filter { it.length > 3 }

fun List<String>.mapCollection() = map { it.uppercase() }

fun List<String>.reduceCollection() = reduce { acc, element -> acc + element }

fun List<String>.foldCollection() = fold(10) { acc, element -> acc + element.length }

fun List<String>.runningReduceCollection() = runningReduce { acc, element -> acc + element.length }

fun List<String>.runningFoldCollection() = runningFold(10) { acc, element -> acc + element.length }


// 6.1.3 Applying a predicate to a collection: all, any, none, count, and find (157/475)

fun List<Int>.allCollection() = all { it > 0 }

fun List<Int>.notAllCollection() = !all { it > 0 }

fun List<Int>.anyCollection() = any { it > 0 }

fun List<Int>.notAnyCollection() = !any { it > 0 }

fun List<Int>.noneCollection() = none { it > 0 }

// Empty collections -> any, none, all (page 158/475)

// Count vs (filter and) size
fun List<Int>.countCollection() = count { it > 0 }

fun List<Int>.sizeCollection() = filter { it > 0 }.size

fun List<Int>.findCollection() = find { it > 0 }


// 6.1.4 Splitting a list into a pair of lists: partition

fun List<Int>.partitionCollection() = partition { it > 0 } // same with filter and filter not

// map of groups
fun List<Int>.groupByCollection() = groupBy { it }

// list to maps
fun List<Int>.associateCollection() = associate { "square of $it" to it * 2 }

fun List<Int>.associateWithCollection() = associateWith { it } // element as key

fun List<Int>.associateByCollection() = associateBy { it } // element as value

// replace all and fill

fun List<Int>.ifEmptyCollection() = ifEmpty { listOf(0) }

// fun List<Int>.ifBlankCollection() = ifBlank { listOf(0) }

// fun List<Int>.ifNullCollection() = ifNull { listOf(0) }

fun List<Int>.windowedCollection() = windowed(3) { it.sum() }

fun List<Int>.chunkedCollection() = chunked(3) { it.sum() }


// 6.1.10 Merging collections: zip

fun List<Int>.zipCollection() = this zip listOf(1, 2, 3, 4, 5)

fun List<List<Int>>.flatMapCollection() = flatMap { it + 2 }

fun List<List<Int>>.flattenCollection() = flatten()

fun List<Int>.toSetCollection() = toSet()
