package day12Part2

import java.io.File
import java.lang.IllegalArgumentException
import day12Part2.Direction.*
import kotlin.math.abs
import utils.*

/*
Challenge at https://adventofcode.com/2020/day/12#part2

Figure out where the navigation instructions actually lead. What is the Manhattan
distance between that location and the ship's starting position?
*/

private var relativeDistanceWE = 0
private var relativeDistanceNS = 0
private var waypoint = Pair(-10, 1)

fun main() {
    val instructions = parseInstrFromFile("in.txt")

    instructions.forEach { instr ->
        when (instr.first) {
            'N', 'W', 'S', 'E' -> moveWaypointToThisDirectionBy(instr.first.toDirection(), instr.second)
            'L', 'R' -> rotateWaypoint(instr.first, instr.second / 90)
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
    val currentDir = System.getProperty("user.dir") + "/src/day12Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val instructions = ArrayList<Pair<Char, Int>>()
    buffReader.forEachLine {
        val direction = it[0]
        val value = it.substring(1).toInt()
        instructions.add(Pair(direction, value))
    }
    return instructions
}

fun moveWaypointToThisDirectionBy(direction: Direction, byThisMuch: Int) {
    var newWaypointWE = waypoint.first
    var newWaypointNS = waypoint.second
    when (direction) {
        NORTH -> newWaypointNS += byThisMuch
        WEST -> newWaypointWE += byThisMuch
        SOUTH -> newWaypointNS -= byThisMuch
        EAST -> newWaypointWE -= byThisMuch
    }
    waypoint = Pair(newWaypointWE, newWaypointNS)
}

fun goForward(byThisMuch: Int) {
    relativeDistanceWE += waypoint.first * byThisMuch
    relativeDistanceNS += waypoint.second * byThisMuch
}

// E.g. to rotate the waypoint to the right by 270 degrees, pass ('R', 3)
fun rotateWaypoint(turnTo: Char, byThisManyTimesNinety: Int) {
    when (turnTo) {
        'R' ->
            for (i in 0 until byThisManyTimesNinety)
                let(waypoint.first, waypoint.second) { (x, y) ->
                    waypoint = Pair(y * -1, x)
                }
        'L' ->
            for (i in 0 until byThisManyTimesNinety)
                let(waypoint.first, waypoint.second) { (x, y) ->
                    waypoint = Pair(y, x * -1)
                }
    }
}