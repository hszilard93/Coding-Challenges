package day8

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/8

Code is represented as a text file with one instruction per line of text.
Each instruction consists of an operation (acc, jmp, or nop) and an argument
(a signed number like +4 or -20).
Run your copy of the boot code. Immediately before any instruction is executed a
second time, what value is in the accumulator?
 */

fun main() {
    val bootCode = parseCodeFromFile("in.txt")
//    bootCode.forEach { println(it) }

    var accumulator = 0L
    var i = 0
    while (i < bootCode.size) {
        bootCode[i].run {
            if (this != null) {
//                println("Processing: $first $second")
                // @formatter:off
                when (first) {
                    "acc" -> { accumulator += second; bootCode[i] = null; i++ }
                    "jmp" -> { bootCode[i] = null; i += second }
                    else -> { bootCode[i] = null; i++ }
                }
                // @formatter:on
            }
            else {
                i = bootCode.size
            }
        }
    }
    println("Before entering infinite loop, the accumulator was: $accumulator")
}

fun parseCodeFromFile(fileName: String): ArrayList<Pair<String, Int>?> {
    val currentDir = System.getProperty("user.dir") + "/src/day8"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val code = ArrayList<Pair<String, Int>?>()
    buffReader.forEachLine { line ->
        val instrRegex = """(nop|acc|jmp) ([+|-]\d+)""".toRegex()
        val matchResult = instrRegex.find(line)
        val instr = matchResult!!.groupValues[1]
        val value = matchResult.groupValues[2].toInt()
        code.add(Pair(instr, value))
    }
    return code
}
