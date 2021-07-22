package day11

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/11

All decisions are based on the number of occupied seats adjacent to a given seat
(one of the eight positions immediately up, down, left, right, or diagonal from the seat).
The following rules are applied to every seat simultaneously:
    If a seat is empty (L) and there are no occupied seats adjacent to it,
    the seat becomes occupied.
    If a seat is occupied (#) and four or more seats adjacent to it are also occupied,
    the seat becomes empty.
    Otherwise, the seat's state does not change.
Floor (.) never changes; seats don't move, and nobody sits on the floor.
Simulate your seating area by applying the seating rules repeatedly until no seats change state.
How many seats end up occupied?
 */

fun main() {
    val initialLayout = readInitialSeatLayoutFromFile("in.txt")
    val stableLayout = calculateStableLayout(initialLayout)
    val solution = countOccupiedSeats(stableLayout)

    println("The number of occupied seats in the stable layout is $solution")
}

fun readInitialSeatLayoutFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day11"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val layout = ArrayList<String>()
    buffReader.forEachLine { layout.add(it) }
    return layout
}

fun calculateStableLayout(layout: ArrayList<String>): ArrayList<String> {
    val oldLayout = ArrayList<String>()
    layout.forEach { oldLayout.add(it) }
    val newLayout = ArrayList<String>()

    var areTheSame = false
    while (!areTheSame) {
        for (i in oldLayout.indices) {
            var newRow = ""
            for (j in oldLayout[i].indices) {
                newRow += if (oldLayout[i][j] == 'L') {
                    val neighbourCount = countNeighbours(oldLayout, i, j)
                    if (neighbourCount == 0) '#' else 'L'
                }
                else if (oldLayout[i][j] == '#') {
                    val neighbourCount = countNeighbours(oldLayout, i, j)
                    if (neighbourCount >= 4) 'L' else '#'
                }
                else
                    '.'
            }
//            println(newRow)
            newLayout.add(newRow)
        }
//        println()
        areTheSame = (newLayout == oldLayout)
        oldLayout.clear()
        newLayout.forEach { oldLayout.add(it) }
        newLayout.clear()
    }
    return oldLayout
}

fun countNeighbours(layout: ArrayList<String>, rowI: Int, colI: Int): Int {
    val minRowI: Int = if (rowI > 0) rowI - 1 else rowI
    val maxRowI: Int = if (rowI < layout.size - 1) rowI + 1 else rowI
    val minColI: Int = if (colI > 0) colI - 1 else colI
    val maxColI: Int = if (colI < layout[rowI].length - 1) colI + 1 else colI

    var neighbourCount = if (layout[rowI][colI] == '#') -1 else 0
    for (i in minRowI..maxRowI)
        for (j in minColI..maxColI)
            if (layout[i][j] == '#')
                neighbourCount += 1

    return neighbourCount
}

fun countOccupiedSeats(layout: ArrayList<String>): Int {
    var occupiedSeatCount = 0
    layout.forEach { row ->
        row.forEach { seat ->
            if (seat == '#') occupiedSeatCount += 1
        }
    }
    return occupiedSeatCount
}