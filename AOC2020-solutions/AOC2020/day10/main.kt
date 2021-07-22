package day10

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/10

Use every adapter in your bag once. What is the distribution of joltage differences between the
charging outlet, the adapters, and your device?
Find a chain that uses all of your adapters to connect the charging outlet to your device's built-in
adapter and count the joltage differences between the charging outlet, the adapters, and your device.
What is the number of 1-jolt differences multiplied by the number of 3-jolt differences?
 */

fun main() {
    val adapters = readAdapterListFromFile("in.txt")
    adapters.sort()

    // The difference between adapter joltages can only be 1, 2 or 3; the last diff will always be 3
    val differences = arrayOf(0, 0, 1)
    var previousAdapter = 0
    adapters.forEach {
//        println(it)
        val difference = it - previousAdapter
        differences[difference - 1] += 1
        previousAdapter = it
    }
//    println("${differences[0]} ${differences[1]} ${differences[2]}")
    val solution = differences[0] * differences[2]
    println("The solution is $solution")
}

fun readAdapterListFromFile(fileName: String): ArrayList<Int> {
    val currentDir = System.getProperty("user.dir") + "/src/day10"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val adapters = ArrayList<Int>()
    buffReader.forEachLine { adapters.add(it.toInt()) }
    return adapters
}