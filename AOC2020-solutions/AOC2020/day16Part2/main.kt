package day16Part2

/*
Challenge at https://adventofcode.com/2020/day/16#part2
*/

import java.io.File
import kotlin.Exception

data class Input(val constraints: List<Constraint>, val ownTicket: List<Int>, val otherTickets: MutableList<List<Int>>)

data class Constraint(val name: String, val limits: List<Pair<Int, Int>>)

fun main() {
    val input = getInputFromFile("in.txt")
//    println(input)
    removeInvalidTickets(input.otherTickets, input.constraints)
//    println(input.otherTickets)
    val constraintFieldMap = matchFieldsToConstraints(input)
    var result = 1L
    constraintFieldMap
            .filter { it.key.startsWith("departure ", true) }
            .forEach { entry ->
                println("${entry.key}: ${input.ownTicket[entry.value]}")
                result *= input.ownTicket[entry.value]
            }
    print(result)
}

fun getInputFromFile(fileName: String): Input {
    val currentDir = System.getProperty("user.dir") + "/src/day16Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    // Read the constraints
    val constraints = ArrayList<Constraint>()
    while (true) {
        val line = buffReader.readLine()
        if (line == "")
            break

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

    val ownTicketLine = buffReader.readLine()
    val ownTicket = ownTicketLine.split(',').map { it.toInt() }

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

fun removeInvalidTickets(tickets: MutableList<List<Int>>, constraints: List<Constraint>) {
    val ticketsToRemove = ArrayList<List<Int>>()
    tickets.forEach { ticket ->
        ticket.forEach { number ->
            var valid = false
            run loop3@{
                constraints.forEach { constraint ->
                    constraint.limits.forEach { limit ->
                        if (number in limit.first..limit.second) {
                            valid = true
                            return@loop3
                        }
                    }
                }
            }
            if (!valid)
                ticketsToRemove.add(ticket)
        }
    }
    tickets.removeIf { it in ticketsToRemove }
}

fun matchFieldsToConstraints(data: Input): Map<String, Int> {
    val constraintFieldMap = HashMap<String, MutableList<Int>>()
    data.apply {
        val allTickets = otherTickets + arrayListOf(ownTicket)
        constraints.forEach { constraint ->
            allTickets[0].indices.forEach { field ->
                var validForAll = true
                run loop3@{
                    allTickets.forEach { ticket ->
                        val number = ticket[field]
                        if (number !in constraint.limits[0].first..constraint.limits[0].second &&
                                number !in constraint.limits[1].first..constraint.limits[1].second) {
                            validForAll = false
                            return@loop3
                        }
                    }
                }
                if (validForAll) {
                    if (!constraintFieldMap.contains(constraint.name))
                        constraintFieldMap[constraint.name] = ArrayList()
                    constraintFieldMap[constraint.name]!!.add(field)
                }
            }
        }
    }

//    println(constraintFieldMap)
    val validMap = calculateValidVariationRecursively(constraintFieldMap)
            ?: throw Exception("Could not generate valid variation!\n$constraintFieldMap")
    println(validMap)
    return validMap
}

fun calculateValidVariationRecursively(inputMap: HashMap<String, MutableList<Int>>,
                                       resultMap: MutableMap<String, Int> = HashMap())
        : MutableMap<String, Int>? {
//    println("inputMap: $inputMap")
    if (inputMap.size == 0)
        return resultMap

    val reducedMap = eliminateSingles(inputMap)

    reducedMap?.forEach { entry ->
//        println("\tentry: $entry")
        val key = entry.key
        run value@ {
            entry.value.forEach { value ->
//                println("\tvalue: $value")
                val newResultMap = HashMap(resultMap)
                newResultMap[key] = value
                val newInputMap = cloneConstraintFieldMap(reducedMap)
                newInputMap.remove(key)
                newInputMap.forEach { e ->
                    e.value.remove(value)
                    if (e.value.size == 0) {
//                        println("\t\t!empty: ${e.key}")
                        return@value
                    }
                }
                val result = calculateValidVariationRecursively(newInputMap, newResultMap)
                if (result != null)
                    return result
            }
        }
    }
//    println("!end")
    return null
}

fun eliminateSingles(inputMap: HashMap<String, MutableList<Int>>): HashMap<String, MutableList<Int>>? {
    val reducedMap = cloneConstraintFieldMap(inputMap)

    var madeChanges = true
    while (reducedMap.values.any { it.size == 1 } && madeChanges) {
        madeChanges = false
        reducedMap.forEach { entry ->
            if (entry.value.size == 1) {
                val value = entry.value[0]
                reducedMap.forEach { newEntry ->
                    if (newEntry != entry) {
                        if (newEntry.value.contains(value)) {
                            madeChanges = true
                            newEntry.value.remove(value)
                        }
                        if (newEntry.value.isEmpty())
                            return null
                    }
                }
            }
        }
    }
    return reducedMap
}

fun cloneConstraintFieldMap(inputMap: HashMap<String, MutableList<Int>>): HashMap<String, MutableList<Int>> {
    val newMap = HashMap<String, MutableList<Int>>()
    inputMap.forEach { e -> newMap[e.key] = ArrayList(e.value) }
    return newMap
}