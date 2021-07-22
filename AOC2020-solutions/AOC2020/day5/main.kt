package day5

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/5

The first 7 characters will either be F or B; these specify exactly one of the 128 rows on
the plane (numbered 0 through 127). Each letter tells you which half of a region the given
seat is in. Start with the whole list of rows; the first letter indicates whether the seat
is in the front (0 through 63) or the back (64 through 127). The next letter indicates which
half of that region the seat is in, and so on until you're left with exactly one row.
Every seat also has a unique seat ID: multiply the row by 8, then add the column.
Look through your list of boarding passes. What is the highest seat ID on a boarding pass?
 */

fun main() {
    val passes = readPassesFromFile("in.txt")

    var maxSeatNumber = 0
    passes.forEach { pass ->
//        println(pass)
        val seatNumber = determineSeatNumberFromSeatCode(pass)
        if (seatNumber > maxSeatNumber) maxSeatNumber = seatNumber
    }

    println("The highest seat number found is $maxSeatNumber")
}

fun readPassesFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day5"
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