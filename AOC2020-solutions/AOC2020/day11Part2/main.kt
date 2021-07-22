package day11Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/11#part2

Now, instead of considering just the eight immediately adjacent seats, consider the first seat
in each of those eight directions. It now takes five or more visible occupied seats for an occupied
seat to become empty.
How many seats end up occupied?
 */

fun main() {
    val initialLayout = readInitialSeatLayoutFromFile("in.txt")
    val stableLayout = calculateStableLayout(initialLayout)
    val solution = countOccupiedSeats(stableLayout)

    println("The number of occupied seats in the new stable layout is $solution")
}

fun readInitialSeatLayoutFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day11Part2"
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
                    if (neighbourCount >= 5) 'L' else '#'
                }
                else
                    '.'
            }
            println(newRow)
            newLayout.add(newRow)
        }
        println()
        areTheSame = (newLayout == oldLayout)
        oldLayout.clear()
        newLayout.forEach { oldLayout.add(it) }
        newLayout.clear()
    }
    return oldLayout
}

fun countNeighbours(layout: ArrayList<String>, rowI: Int, colI: Int): Int {
    var neighbourCount = 0
    if (hasNeighbourToLeft(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToRight(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToUp(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToDown(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToLeftUp(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToLeftDown(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToRightUp(layout, rowI, colI)) neighbourCount++
    if (hasNeighbourToRightDown(layout, rowI, colI)) neighbourCount++

    return neighbourCount
}

fun hasNeighbourToLeft(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    if (colI > 0)
        for (c in colI - 1 downTo 0)
            if (layout[rowI][c] == '#')
                return true
            else if (layout[rowI][c] == 'L')
                return false
    return false
}

fun hasNeighbourToRight(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    if (colI < layout[rowI].length - 1)
        for (c in colI + 1 until layout[rowI].length)
            if (layout[rowI][c] == '#')
                return true
            else if (layout[rowI][c] == 'L')
                return false
    return false
}

fun hasNeighbourToUp(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    if (rowI > 0)
        for (r in rowI - 1 downTo 0)
            if (layout[r][colI] == '#')
                return true
            else if (layout[r][colI] == 'L')
                return false
    return false
}

fun hasNeighbourToDown(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    if (rowI < layout.size - 1)
        for (r in rowI + 1 until layout.size)
            if (layout[r][colI] == '#')
                return true
            else if (layout[r][colI] == 'L')
                return false
    return false
}

fun hasNeighbourToLeftUp(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    var r = rowI - 1
    var c = colI - 1
    while (r >= 0 && c >= 0) {
        if (layout[r][c] == '#')
            return true
        else if (layout[r][c] == 'L')
            return false
        r -= 1
        c -= 1
    }
    return false
}

fun hasNeighbourToRightUp(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    var r = rowI - 1
    var c = colI + 1
    while (r >= 0 && c < layout[r].length) {
        if (layout[r][c] == '#')
            return true
        else if (layout[r][c] == 'L')
            return false
        r -= 1
        c += 1
    }
    return false
}

fun hasNeighbourToLeftDown(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    var r = rowI + 1
    var c = colI - 1
    while (r < layout.size && c >= 0) {
        if (layout[r][c] == '#')
            return true
        else if (layout[r][c] == 'L')
            return false
        r += 1
        c -= 1
    }
    return false
}

fun hasNeighbourToRightDown(layout: ArrayList<String>, rowI: Int, colI: Int): Boolean {
    var r = rowI + 1
    var c = colI + 1
    while (r < layout.size && c < layout[r].length) {
        if (layout[r][c] == '#')
            return true
        else if (layout[r][c] == 'L')
            return false
        r += 1
        c += 1
    }
    return false
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