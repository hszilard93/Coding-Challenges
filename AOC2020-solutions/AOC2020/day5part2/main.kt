package day5part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/5#part2

Some of the seats at the very front and back of the plane don't exist on this aircraft,
so they'll be missing from your list as well.
Your seat wasn't at the very front or back, though; the seats with IDs +1 and -1 from yours
will be in your list.
What is the ID of your seat?
 */

fun main() {
    val seatCodes = readSeatCodesFromFile("in.txt")

    val seatsArray = BooleanArray(1023)     // The maximum number of seats possible, 127 * 8 + 7
    seatCodes.forEach { code ->
//        println(code)
        val seatNumber = determineSeatNumberFromSeatCode(code)
        seatsArray[seatNumber] = true
    }

    var emptySeatInMiddle: Int = -1
    for (i in 1..1021) {
        if (!seatsArray[i] && seatsArray[i-1] && seatsArray[i+1])
            emptySeatInMiddle = i
    }
    println("Your seat is $emptySeatInMiddle")
}

fun readSeatCodesFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day5Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val passes = ArrayList<String>()
    buffReader.forEachLine {
        passes.add(it)
    }
    return passes
}

fun determineSeatNumberFromSeatCode(seatCode: String): Int {
    val rowCode = seatCode.substring(0, 7)
    val rowNumber = determineRowNumberFromRowCode(rowCode)
    val colCode = seatCode.substring(7, 10)
    val colNumber = determineColNumberFromColCode(colCode)
    val seatNumber = rowNumber * 8 + colNumber
//    println("Row:$rowNumber Col:$colNumber Seat:$seatNumber")
    return seatNumber
}

fun determineRowNumberFromRowCode(rowCode: String): Int {
    var lowerBound = 0
    var upperBound = 127
    rowCode.forEach {
        when (it) {
            'F' -> upperBound = upperBound - (upperBound - lowerBound) / 2 - 1
            'B' -> lowerBound = lowerBound + (upperBound - lowerBound) / 2 + 1
            else -> throw Exception("Invalid row code: $it")
        }
    }
    return lowerBound
}

fun determineColNumberFromColCode(colCode: String): Int {
    var lowerBound = 0
    var upperBound = 7
    colCode.forEach {
        when (it) {
            'L' -> upperBound = upperBound - (upperBound - lowerBound) / 2 - 1
            'R' -> lowerBound = lowerBound + (upperBound - lowerBound) / 2 + 1
            else -> throw Exception("Invalid col code: $it")
        }
    }
    return lowerBound
}