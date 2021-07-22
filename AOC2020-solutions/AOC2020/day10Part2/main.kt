package day10Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/10#part2

What is the total number of distinct ways you can arrange the adapters to connect
the charging outlet to your device?
 */

fun main() {
    val adapters = readAdapterListFromFile("in.txt")
    adapters.add(0)
    adapters.sort()

    val solution = countAllLegalVariationsWithMemoization(adapters)
    println("The solution is $solution")
}

fun readAdapterListFromFile(fileName: String): ArrayList<Int> {
    val currentDir = System.getProperty("user.dir") + "/src/day10Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val adapters = ArrayList<Int>()
    buffReader.forEachLine { adapters.add(it.toInt()) }
    return adapters
}

fun countAllLegalVariationsNoRecursion(adapters: List<Int>): Long {
    var variations = 1L
    for (i in 0 until adapters.size - 1) {
        if (adapters[i + 1] - adapters[i] > 3)
            throw Exception("Error at adapters ${adapters[i]} and ${adapters[i + 1]}")

        if (i + 2 < adapters.size && adapters[i + 2] - adapters[i] < 3)
            variations *= 2

        if (i + 3 < adapters.size && adapters[i + 3] - adapters[i] < 3)
            variations *= 2
    }
    return variations
}

// This is neat, but still not the best solution, I think
fun countAllLegalVariationsWithMemoization(
    adapters: List<Int>,
    memo: HashMap<List<Int>, Long> = HashMap()
): Long {
    //    adapters.forEach { print("$it ") }
    //    println()
    var variations = 1L
    for (i in 0 until adapters.size - 1) {
        if (adapters[i + 1] - adapters[i] > 3)
            throw Exception("Error at adapters ${adapters[i]} and ${adapters[i + 1]}")

        if (i + 2 < adapters.size && adapters[i + 2] - adapters[i] <= 3) {
            val subList = adapters.subList(i + 2, adapters.size)
            variations += if (memo.contains(subList))
                memo[subList]!!
            else
                countAllLegalVariationsWithMemoization(subList, memo)
        }
        if (i + 3 < adapters.size && adapters[i + 3] - adapters[i] <= 3) {
            val subList = adapters.subList(i + 3, adapters.size)
            variations += if (memo.contains(subList))
                memo[subList]!!
            else
                countAllLegalVariationsWithMemoization(subList, memo)
        }
    }
    memo[adapters] = variations
    return variations
}