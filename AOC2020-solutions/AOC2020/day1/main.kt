package day1

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/1

find the two entries that sum to 2020 and then multiply those two numbers together.
*/

fun main() {
    val inArray = ArrayList<Int>()
    val currentDir = System.getProperty("user.dir") + "/src/day1"
    val buffReader = File("$currentDir/in.txt").bufferedReader()

    buffReader.forEachLine { inArray.add(it.toInt()) }

    for (i in 0 until inArray.size) {
        for (j in i until inArray.size)
            if (inArray[i] + inArray[j] == 2020) {
                println("The solution is ${inArray[i] * inArray[j]}")
                return
            }
    }

    println("No solution found")
    return
}