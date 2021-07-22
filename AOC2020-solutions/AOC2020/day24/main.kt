package day24

/*
Challenge at https://adventofcode.com/2020/day/24
 */

import day24.Color.BLACK
import day24.Direction.*
import java.io.File
import java.lang.IllegalArgumentException

enum class Direction {
    NE, E, SE, SW, W, NW;

    companion object {
        fun fromString(input: String): Direction {
            return when (input.uppercase()) {
                "NE" -> NE
                "E" ->  E
                "SE" -> SE
                "SW" -> SW
                "W" ->  W
                "NW" -> NW
                else -> throw IllegalArgumentException("Unable to convert $input to Direction!")
            }
        }
    }
}

enum class Color {
    BLACK, WHITE
}

class Tile(val coordinates: Pair<Int, Int>) {
    var color = Color.WHITE

    fun flipColor() {
        color = if (color == Color.WHITE) BLACK else Color.WHITE
    }
}

fun main() {
    val directionList = readInstructionsFromFile("in.txt")
    val traversedTiles = HashMap<Pair<Int, Int>, Tile>()

    val centerTile = Tile(Pair(0, 0))
    traversedTiles[centerTile.coordinates] = centerTile
    directionList.forEach { directions ->
        var currentTile = centerTile
        directions.forEach { direction ->
            val nextCoordinates = calculateNewCoordinates(currentTile.coordinates, direction)
            if (nextCoordinates !in traversedTiles) {
                val nextTile = Tile(nextCoordinates)
                traversedTiles[nextCoordinates] = nextTile
            }
            currentTile = traversedTiles[nextCoordinates]!!
        }
        currentTile.flipColor()
    }

    val blackCount = traversedTiles.values.fold(0) { acc, tile -> if (tile.color == BLACK) acc + 1 else acc }
    println("The number of black tiles will be: $blackCount")
}

fun calculateNewCoordinates(coordinates: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
    return when (direction) {
        NE -> if (coordinates.second % 2 == 0) Pair(coordinates.first, coordinates.second - 1) else Pair(coordinates.first + 1, coordinates.second - 1)
        E -> Pair(coordinates.first + 1, coordinates.second)
        SE -> if (coordinates.second % 2 == 0) Pair(coordinates.first, coordinates.second + 1) else Pair(coordinates.first + 1, coordinates.second + 1)
        SW -> if (coordinates.second % 2 == 0) Pair(coordinates.first - 1, coordinates.second + 1) else Pair(coordinates.first, coordinates.second + 1)
        W -> Pair(coordinates.first - 1, coordinates.second)
        NW -> if (coordinates.second % 2 == 0) Pair(coordinates.first - 1, coordinates.second - 1) else Pair(coordinates.first, coordinates.second - 1)
    }
}

fun readInstructionsFromFile(fileName: String): List<List<Direction>> {
    val directionsList = ArrayList<List<Direction>>()

    val currentDir = System.getProperty("user.dir") + "/src/day24"
    val buffReader = File("$currentDir/$fileName").bufferedReader()
    buffReader.forEachLine { line ->
        val directions = ArrayList<Direction>()
        val directionRegex = """(ne)|(se)|(nw)|(sw)|(n)|(e)|(s)|(w)""".toRegex()
        val matchResults = directionRegex.findAll(line)
        matchResults.forEach { result ->
            directions.add(Direction.fromString(result.groupValues[0]))
        }
        directionsList.add(directions)
    }
    return directionsList
}