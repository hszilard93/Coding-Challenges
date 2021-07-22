package day17Part2

/*
Challenge at https://adventofcode.com/2020/day/17#part2
*/

import utils.Quad
import java.io.File

typealias CubeMap = HashMap<Quad<Int, Int, Int, Int>, Boolean>

private data class Environment(var minX: Int = 0, var maxX: Int = 0,
                               var minY: Int = 0, var maxY: Int = 0,
                               var minZ: Int = 0, var maxZ: Int = 0,
                               var minW: Int = 0, var maxW: Int = 0)

private val e = Environment()

fun main() {
    var cubeMap = getCubeMapFromFile("in.txt")
    println("Starting state:")
    printCubeMap(cubeMap, true)
    for (i in 1..6) {
        cubeMap = generateNextCycle(cubeMap)
        println("Cycle #$i:")
        printCubeMap(cubeMap)
    }
    val count = cubeMap.count { it.value }
    println(count)
}

fun getCubeMapFromFile(fileName: String): CubeMap {
    val currentDir = System.getProperty("user.dir") + "/src/day17Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val cubeMap = CubeMap()
    var i = 0
    buffReader.lines().forEach { line ->
        if (i > e.maxX)
            e.maxX = i
        var j = 0
        line.forEach { char ->
            if (j > e.maxY)
                e.maxY = j
            when (char) {
                '.' -> cubeMap[Quad(i, j, 0, 0)] = false
                '#' -> cubeMap[Quad(i, j, 0, 0)] = true
            }
            j += 1
        }
        i += 1
    }
    return cubeMap
}

fun generateNextCycle(inputMap: CubeMap): CubeMap {
    val resultMap = CubeMap(inputMap)
    for (i in e.minX - 1..e.maxX + 1)
        for (j in e.minY - 1..e.maxY + 1)
            for (k in e.minZ - 1..e.maxZ + 1)
                for (l in e.minW - 1..e.maxW + 1) {
                    Quad(i, j, k, l).let { coordinates ->
                        val neighbors = countNeighbours(inputMap, coordinates)
                        if (neighbors == 3 && inputMap[coordinates] != true)
                            resultMap[coordinates] = true
                        else if (!(inputMap[coordinates] == true && neighbors in 2..3))
                            if (inputMap[coordinates] != null)
                                resultMap[coordinates] = false
                    }
                }

    resultMap.keys.apply {
        e.minX = minOf { it.first }
        e.maxX = maxOf { it.first }
        e.minY = minOf { it.second }
        e.maxY = maxOf { it.second }
        e.minZ = minOf { it.third }
        e.maxZ = maxOf { it.third }
        e.minW = minOf { it.fourth }
        e.maxW = maxOf { it.fourth }
    }

    for (i in e.minX..e.maxX)
        for (j in e.minY..e.maxY)
            for (k in e.minZ..e.maxZ)
                for (l in e.minW..e.maxW)
                    if (resultMap[Quad(i, j, k, l)] == null)
                        resultMap[Quad(i, j, k, l)] = false

    return resultMap
}

fun printCubeMap(cubeMap: CubeMap, showNeighbors: Boolean = false) {
    for (l in e.minW..e.maxW) {
        println("W = $l")
        for (k in e.minZ..e.maxZ) {
            println("Z = $k")
            for (i in e.minX..e.maxX) {
                for (j in e.minY..e.maxY) {
                    print(if (cubeMap[Quad(i, j, k, l)] == true) '#' else '.')
                    if (showNeighbors)
                        print("|${countNeighbours(cubeMap, Quad(i, j, k, l))}| ")
                }
                println()
            }
            println("--------")
        }
        println("+*+*+*+*+*+*+*")
    }
}

fun countNeighbours(cubeMap: CubeMap, coordinates: Quad<Int, Int, Int, Int>): Int {
    var count = if (cubeMap[coordinates] != null && cubeMap[coordinates]!!) -1 else 0
    for (i in coordinates.first - 1..coordinates.first + 1)
        for (j in coordinates.second - 1..coordinates.second + 1)
            for (k in coordinates.third - 1..coordinates.third + 1)
                for (l in coordinates.fourth - 1..coordinates.fourth + 1)
                    if (cubeMap[Quad(i, j, k, l)] == true)
                        count++

    return count
}