package day2Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/2#part2

Each policy actually describes two positions in the password,
where 1 means the first character, 2 means the second character,
and so on. (Be careful; Toboggan Corporate Policies have no concept of "index zero"!)
Exactly one of these positions must contain the given letter.
Other occurrences of the letter are irrelevant for the purposes of policy enforcement.
How many passwords are valid according to the new interpretation of the policies?
*/

fun main() {
    var validCounter = 0

    val currentDir = System.getProperty("user.dir") + "/src/day2"
    val buffReader = File("$currentDir/in.txt").bufferedReader()

    buffReader.forEachLine {
        val patternLine = """(\d*)-(\d*)\s([a-z]):\s(\w*)""".toRegex()
        val matchResult = patternLine.find(it)!!

        val pos1 = matchResult.groupValues[1].toInt()
        val pos2 = matchResult.groupValues[2].toInt()
        val key: Char = matchResult.groupValues[3][0]    // it's always just the one letter
        val pass = matchResult.groupValues[4]
        println("$pos1 | $pos2 | $key | $pass")

        val char1 = if (pass.length >= pos1 - 1) pass[pos1 - 1] else null
        val char2 = if (pass.length >= pos2 - 1) pass[pos2 - 1] else null
        // Check if either but not both chars at the given indices are equal to key
        if ((char1 == key || char2 == key) &&
            (char1 != key || char2 != key)) {
            println("Valid: $it | $char1 | $char2")
            validCounter++
        }
    }

    println("\nThe number of valid passwords is $validCounter")
}