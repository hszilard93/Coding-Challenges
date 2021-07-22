package day8Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/8#part2

Fix the program so that it terminates normally by changing exactly one jmp (to nop) or nop (to jmp).
What is the value of the accumulator after the program terminates?
Did try a more elegant solution, but gotta brute force it.
 */

fun main() {
    val bootCode = parseCodeFromFile("in.txt")
    //    bootCode.forEach { println(it) }
    val accumulator = repairCode(bootCode)
    if (accumulator != null)
        println("The correct result is: $accumulator")
    else
        println("Did not find a solution.")
}

fun parseCodeFromFile(fileName: String): ArrayList<Pair<String, Int>> {
    val currentDir = System.getProperty("user.dir") + "/src/day8Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val code = ArrayList<Pair<String, Int>>()
    buffReader.forEachLine { line ->
        val instrRegex = """(nop|acc|jmp) ([+|-]\d+)""".toRegex()
        val matchResult = instrRegex.find(line)
        val instr = matchResult!!.groupValues[1]
        val value = matchResult.groupValues[2].toInt()
        code.add(Pair(instr, value))
    }
    return code
}

fun repairCode(oldCode: ArrayList<Pair<String, Int>>): Int? {
    // Let's make a copy of the existing code
    val newCode = ArrayList<Pair<String, Int>>()
    oldCode.forEach { newCode.add(Pair(it.first, it.second)) }

    // Attempt to replace every jmp and nop instruction
    for (i in 0 until newCode.size) {
        if (newCode[i].first == "jmp" || newCode[i].first == "nop") {
            println("Changing out intruction: ${newCode[i].first} ${newCode[i].second} at line ${i + 1}")
            when (newCode[i].first) {
                "jmp" -> newCode[i] = Pair("nop", newCode[i].second)
                "nop" -> newCode[i] = Pair("jmp", newCode[i].second)
            }
            try {
                return executeCode(newCode)     // Does it work?
            } catch (e: Exception) {            // It dinnae work, change it back!
                newCode[i] = oldCode[i]
            }
        }
    }
    return null
}

fun executeCode(code: ArrayList<Pair<String, Int>>): Int {
    val isInstrVisitedArr = BooleanArray(code.size)
    var accumulator = 0
    var i = 0
    while (i < code.size) {
        code.let {
            if (!isInstrVisitedArr[i]) {
//                println("Processing: ${it[i].first} ${it[i].second} | Line number: ${i + 1}")
                // @formatter:off
                when (it[i].first) {
                    "acc" -> { accumulator += it[i].second; isInstrVisitedArr[i] = true; i++ }
                    "jmp" -> { isInstrVisitedArr[i] = true; i += it[i].second }
                    else -> { isInstrVisitedArr[i] = true; i++ }
                }
                // @formatter:on
            }
            else {
                throw Exception("The code is still faulty! Loopy at line ${i + 1}")
            }
        }
    }
    return accumulator
}