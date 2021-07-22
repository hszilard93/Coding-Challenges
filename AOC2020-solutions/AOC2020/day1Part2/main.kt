package day1Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/1#part2

find three numbers in your expense report that meet the same criteria.
*/

fun main() {
    val inArray = ArrayList<Int>()
    val currentDir = System.getProperty("user.dir") + "/src/day1Part2"
    val buffReader = File("$currentDir/in.txt").bufferedReader()

    buffReader.forEachLine { inArray.add(it.toInt()) }

    for (i in 0 until inArray.size) {
        for (j in i until inArray.size)
            if (inArray[i] + inArray[j] < 2020)
                for (k in j until inArray.size)
                    if (inArray[i] + inArray[j] + inArray[k] == 2020) {
                        println("The solution is ${inArray[i] * inArray[j] * inArray[k]}")
                        return
                    }
    }

    println("No solution found")
    return
}