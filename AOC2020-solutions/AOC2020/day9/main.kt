package day9

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/9

XMAS starts by transmitting a preamble of 25 numbers. After that, each number you receive
should be the sum of any two of the 25 immediately previous numbers. The two numbers will
have different values, and there might be more than one such pair.
Find the first number in the list (after the preamble) which is not the sum of two of the 25
numbers before it. What is the first number that does not have this property?
*/

fun main() {
    val currentDir = System.getProperty("user.dir") + "/src/day9"
    val buffReader = File("$currentDir/in.txt").bufferedReader()

    val preambleArr = LongArray(25)     // Rolling register of the previous 25 numbers
    for (i in preambleArr.indices)
        preambleArr[i] = buffReader.readLine().toLong()

    var firstSolution: Long? = null
    var preambleI = 0
    whileLoop@
    while (firstSolution == null && buffReader.ready()) {
        val newNumber = buffReader.readLine().toLong()
        for (i in preambleArr.indices)
            for (j in i until preambleArr.size)
                if (newNumber == preambleArr[i] + preambleArr[j]) {
                    preambleArr[preambleI] = newNumber
                    preambleI = if (preambleI == preambleArr.size - 1) 0 else preambleI + 1
                    continue@whileLoop
                }
        firstSolution = newNumber
    }

    println("The first solution is $firstSolution")
}