package day16

/*
Challenge at https://adventofcode.com/2020/day/16#part2
*/

import java.io.File

data class Constraint(val name: String, val limits: List<Pair<Int, Int>>)

data class Input(val constraints: List<Constraint>, val ownTicket: List<Int>, val otherTickets: List<List<Int>>)

fun main() {
    val input = getInputFromFile("in.txt")
//    println(input)
    val invalidNumbers = ArrayList<Int>()
    input.otherTickets.forEach { ticket ->
        ticket.forEach { number ->
            var valid = false
            run loop3@{
                input.constraints.forEach { constraint ->
                    constraint.limits.forEach { limit ->
                        if (number in limit.first..limit.second) {
                            valid = true
                            return@loop3
                        }
                    }
                }
            }
            if (!valid) {
                invalidNumbers.add(number)
            }
        }
    }
    println(invalidNumbers.sum())
}

fun getInputFromFile(fileName: String): Input {
    val currentDir = System.getProperty("user.dir") + "/src/day16"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    // Read the constraints
    val constraints = ArrayList<Constraint>()
    while (true) {
        val line = buffReader.readLine()
        if (line == "")
            break;

        val expr = """^([\w\s]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
        val matchResult = expr.find(line)
        matchResult!!.apply {
            val constraint = Constraint(groupValues[1],
                                        arrayListOf(Pair(groupValues[2].toInt(), groupValues[3].toInt()),
                                                    Pair(groupValues[4].toInt(), groupValues[5].toInt())
                                        ))
            constraints.add(constraint)
        }
    }

    // Read own ticket
    do {
        val line = buffReader.readLine()
    } while (line != "your ticket:")
    val line = buffReader.readLine()
    val ownTicket = line.split(',').map { it.toInt() }

    // Read the other tickets
    val otherTickets = ArrayList<List<Int>>()
    do {
        val line = buffReader.readLine()
    } while (line != "nearby tickets:")

    while (true) {
        val line = buffReader.readLine() ?: break
        val ticket = line.split(',').map { it.toInt() }
        otherTickets.add(ticket)
    }

    return Input(constraints, ownTicket, otherTickets)
}
