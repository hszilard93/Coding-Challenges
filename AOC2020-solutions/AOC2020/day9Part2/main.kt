package day9Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/9#part2

You must find a contiguous set of at least two numbers in your list
which sum to the invalid number from step 1.
Add together the smallest and largest number in this contiguous range;
in this example, these are 15 and 47, producing 62.
*/

fun main() {
    val numbers = readNumbersFromFile("in.txt")
    val firstError = findFirstInvalidNumber(numbers)
    val contList = findContSetOfSum(firstError, numbers)

//    println("The first solution is $firstError")
//    println("Contiguous set with sum of $firstError found:")
//    contList.forEach { println(it) }

    var minVal = Long.MAX_VALUE
    var maxVal = Long.MIN_VALUE
    contList.forEach {
        if (it < minVal) minVal = it
        if (it > maxVal) maxVal = it
    }
    val solution = minVal + maxVal
    println("The solution is $solution")
}

fun readNumbersFromFile(fileName: String): ArrayList<Long> {
    val currentDir = System.getProperty("user.dir") + "/src/day9Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()
    val arrayList = ArrayList<Long>()
    buffReader.forEachLine {
        arrayList.add(it.toLong())
    }
    return arrayList
}

fun findFirstInvalidNumber(numbers: ArrayList<Long>): Long {
    val preambleArr = LongArray(25)     // Rolling register of the previous 25 numbers
    for (i in preambleArr.indices)
        preambleArr[i] = numbers[i]

    var firstSolution: Long? = null
    var preambleI = 0
    var numbersI = preambleArr.size
    whileLoop@
    while (firstSolution == null && numbersI < numbers.size) {
        val currentNumber = numbers[numbersI]
        for (i in preambleArr.indices)
            for (j in i until preambleArr.size)
                if (currentNumber == preambleArr[i] + preambleArr[j]) {
                    preambleArr[preambleI] = currentNumber
                    preambleI = if (preambleI == preambleArr.size - 1) 0 else preambleI + 1
                    numbersI += 1
                    continue@whileLoop
                }
        firstSolution = currentNumber
    }
    return firstSolution ?: throw Exception("No invalid number found.")
}

fun findContSetOfSum(sum: Long, numbers: ArrayList<Long>): List<Long> {
    outerForLoop@
    for (i in 0 until numbers.size) {
        var sumOfThisRange = numbers[i]
        for (j in i + 1 until numbers.size) {
            sumOfThisRange += numbers[j]
            if (sumOfThisRange == sum)
                return numbers.subList(i, j + 1)
            else if (sumOfThisRange > sum)
                continue@outerForLoop
        }
    }
    throw Exception("No contiguous set of sum $sum found.")
}