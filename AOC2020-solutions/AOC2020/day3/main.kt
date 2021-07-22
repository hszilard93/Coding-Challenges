package day3

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/3

Starting at the top-left corner of your map
and following a slope of right 3 and down 1,
how many trees would you encounter?
 */

fun main() {
    val goRightBy = 3
    val goDownBy = 1

    val map = readMapFromFile("in.txt")
    val mapSizeY = map.size
    val mapSizeX = map[0].size

    var posX = 0
    var posY = 0
    var treeCounter = 0
    while (posY < mapSizeY) {
        if (map[posY][posX] == '#') treeCounter++
//        map[posY][posX] = 'U'
        posY += goDownBy
        val absPosX = posX + goRightBy
        posX = if (absPosX < mapSizeX) absPosX else absPosX - mapSizeX  // Handling 'map overflow'
    }

//    map.forEach{ println(it) }
    println("You would encounter this many trees: $treeCounter")
}

fun readMapFromFile(fileName: String): ArrayList<CharArray> {
    val currentDir = System.getProperty("user.dir") + "/src/day3"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val map = ArrayList<CharArray>()
    buffReader.forEachLine { map.add(it.toCharArray()) }
    return map
}