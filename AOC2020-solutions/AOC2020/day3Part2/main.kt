package day3Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/3#part2

Determine the number of trees you would encounter if, for each of the following slopes,
you start at the top-left corner and traverse the map all the way to the bottom:
    Right 1, down 1.
    Right 3, down 1. (This is the slope you already checked.)
    Right 5, down 1.
    Right 7, down 1.
    Right 1, down 2.
What do you get if you multiply together the number of trees encountered on each
of the listed slopes?
*/

fun main() {
    val map = readMapFromFile("in.txt")
    val mapSizeY = map.size
    val mapSizeX = map[0].size

    val slopes = arrayListOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
    var prodOfTreesEncountered = 1L

    slopes.forEach {
        var treeCounter = 0
        val goRightBy = it.first
        val goDownBy = it.second
        var posX = 0
        var posY = 0
        while (posY < mapSizeY) {
            if (map[posY][posX] == '#') treeCounter++
            posY += goDownBy
            val absPosX = posX + goRightBy
            posX = if (absPosX < mapSizeX) absPosX else absPosX - mapSizeX  // Handling 'map overflow'
        }
        map.forEach { line -> println(line) }
//        println("Trees encountered on this run: $treeCounter")
        prodOfTreesEncountered *= treeCounter
    }

    println("The product of trees encountered on each slope is $prodOfTreesEncountered")
}

fun readMapFromFile(fileName: String): ArrayList<CharArray> {
    val currentDir = System.getProperty("user.dir") + "/src/day3Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val map = ArrayList<CharArray>()
    buffReader.forEachLine { map.add(it.toCharArray()) }
    return map
}