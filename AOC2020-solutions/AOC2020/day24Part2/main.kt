package day24Part2

/*
Challenge at https://adventofcode.com/2020/day/24#part2
 */

import day24Part2.Color.BLACK
import day24Part2.Direction.*
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
    val tileMap = HashMap<Pair<Int, Int>, Tile>()
    doInitialSetup(directionList, tileMap)

    for (i in 1..100) {
        fillOutTileMap(tileMap)
        simulateNextDay(tileMap)
    }

    val blackCount = tileMap.values.fold(0) { acc, tile -> if (tile.color == BLACK) acc + 1 else acc }
    println("After 100 days, the number of black tiles will be: $blackCount")
}

fun doInitialSetup(directionList: List<List<Direction>>, tileMap: HashMap<Pair<Int, Int>, Tile>) {
    val centerTile = Tile(Pair(0, 0))
    tileMap[centerTile.coordinates] = centerTile
    directionList.forEach { directions ->
        var currentTile = centerTile
        directions.forEach { direction ->
            val nextCoordinates = calculateNeighborCoordinates(currentTile.coordinates, direction)
            if (nextCoordinates !in tileMap) {
                tileMap[nextCoordinates] = Tile(nextCoordinates)
            }
            currentTile = tileMap[nextCoordinates]!!
        }
        currentTile.flipColor()
    }
}

// We make sure that all black tiles have neighbors
fun fillOutTileMap(tileMap: HashMap<Pair<Int, Int>, Tile>) {
    val coordinatesOfTilesToAdd = HashSet<Pair<Int, Int>>()
    tileMap.filter { e -> e.value.color == BLACK }.forEach { entry ->
        neighborCoordinates(entry.key).forEach { neighborCoordinate ->
            if (neighborCoordinate !in tileMap) {
                coordinatesOfTilesToAdd.add(neighborCoordinate)
            }
        }
    }
    coordinatesOfTilesToAdd.forEach { coordinate ->
        tileMap[coordinate] = Tile(coordinate)
    }
}

fun simulateNextDay(tileMap: HashMap<Pair<Int, Int>, Tile>) {
    val tilesToFlip = HashSet<Tile>()
    tileMap.forEach { (coordinate, tile) ->
        val neighborCoordinates = neighborCoordinates(coordinate)
        val blackNeighborCount = neighborCoordinates.fold(0) { acc, c -> if (tileMap[c]?.color == BLACK) acc + 1 else acc }
        when (tile.color) {
            BLACK ->
                if (blackNeighborCount == 0 || blackNeighborCount > 2) {
                    tilesToFlip.add(tile)
                }
            Color.WHITE ->
                if (blackNeighborCount == 2) {
                    tilesToFlip.add(tile)
                }
        }
    }

    tilesToFlip.forEach { tile -> tile.flipColor() }
}

fun neighborCoordinates(coordinates: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val neighborCoordinates = HashSet<Pair<Int, Int>>()
    Direction.values().forEach { direction ->
        neighborCoordinates.add(calculateNeighborCoordinates(coordinates, direction))
    }
    return neighborCoordinates
}

fun calculateNeighborCoordinates(coordinates: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
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

    val currentDir = System.getProperty("user.dir") + "/src/day24Part2"
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