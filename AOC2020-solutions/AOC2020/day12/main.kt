package day12

import java.io.File
import java.lang.IllegalArgumentException
import day12.Direction.*
import kotlin.math.abs

/*
Challenge at https://adventofcode.com/2020/day/12

The navigation instructions (your puzzle input) consists of a sequence of single-character
actions paired with integer input values. The ship starts by facing east.
Figure out where the navigation instructions lead. What is the Manhattan distance
between that location and the ship's starting position?
*/

private var relativeDistanceWE = 0
private var relativeDistanceNS = 0
private var heading = EAST

fun main() {
    val instructions = parseInstrFromFile("in.txt")

    instructions.forEach { instr ->
        when (instr.first) {
            'N', 'W', 'S', 'E' -> goToThisDirectionBy(instr.first.toDirection(), instr.second)
            'L', 'R' -> changeHeading(instr.first, instr.second)
            'F' -> goForward(instr.second)
            else -> throw Exception("Illegal character: ${instr.first}")
        }
    }

    val manhattanDistance = abs(relativeDistanceNS) + abs(relativeDistanceWE)
    println("You have gone North by $relativeDistanceNS and West by $relativeDistanceWE.")
    println("The Manhattan distance relative to your starting position is $manhattanDistance.")
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

fun Char.toDirection(): Direction {
    return when (this) {
        'N' -> NORTH
        'W' -> WEST
        'S' -> SOUTH
        'E' -> EAST
        else -> throw IllegalArgumentException("Cannot convert Char '$this' to a Direction!")
    }
}

fun parseInstrFromFile(fileName: String): ArrayList<Pair<Char, Int>> {
    val currentDir = System.getProperty("user.dir") + "/src/day12"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val instructions = ArrayList<Pair<Char, Int>>()
    buffReader.forEachLine {
        val direction = it[0]
        val value = it.substring(1).toInt()
        instructions.add(Pair(direction, value))
    }
    return instructions
}

fun goToThisDirectionBy(direction: Direction, byThisMuch: Int) {
    when (direction) {
        NORTH -> relativeDistanceNS += byThisMuch
        WEST -> relativeDistanceWE += byThisMuch
        SOUTH -> relativeDistanceNS -= byThisMuch
        EAST -> relativeDistanceWE -= byThisMuch
    }
}

fun goForward(byThisMuch: Int) {
    goToThisDirectionBy(heading, byThisMuch)
}

fun changeHeading(turnTo: Char, byThisMuch: Int) {
    when (turnTo) {
        'L' ->
            when (heading) {
                NORTH ->
                    heading = when (byThisMuch) {
                        90 -> WEST
                        180 -> SOUTH
                        270 -> EAST
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
                WEST ->
                    heading = when (byThisMuch) {
                        90 -> SOUTH
                        180 -> EAST
                        270 -> NORTH
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
                SOUTH ->
                    heading = when (byThisMuch) {
                        90 -> EAST
                        180 -> NORTH
                        270 -> WEST
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
                EAST ->
                    heading = when (byThisMuch) {
                        90 -> NORTH
                        180 -> WEST
                        270 -> SOUTH
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
            }
        'R' ->
            when (heading) {
                NORTH ->
                    heading = when (byThisMuch) {
                        90 -> EAST
                        180 -> SOUTH
                        270 -> WEST
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
                WEST ->
                    heading = when (byThisMuch) {
                        90 -> NORTH
                        180 -> EAST
                        270 -> SOUTH
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
                SOUTH ->
                    heading = when (byThisMuch) {
                        90 -> WEST
                        180 -> NORTH
                        270 -> EAST
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
                EAST ->
                    heading = when (byThisMuch) {
                        90 -> SOUTH
                        180 -> WEST
                        270 -> NORTH
                        else -> throw IllegalArgumentException("Can't change direction by $byThisMuch, $turnTo")
                    }
            }
        else -> throw IllegalArgumentException("Could not turn from $heading to $turnTo by $byThisMuch!")
    }
}