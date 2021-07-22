package day2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/2

Each line gives the password policy and then the password.
The password policy indicates the lowest and highest number of
times a given letter must appear for the password to be valid.
For example, 1-3 a means that the password must contain a at
least 1 time and at most 3 times.
How many passwords are valid according to their policies?
*/

fun main() {
    var validCounter = 0

    val currentDir = System.getProperty("user.dir") + "/src/day2"
    val buffReader = File("$currentDir/in.txt").bufferedReader()

    buffReader.forEachLine {
        val patternLine = """(\d*)-(\d*)\s([a-z]):\s(\w*)""".toRegex()
        val matchResult = patternLine.find(it)!!

        val min = matchResult.groupValues[1].toInt()
        val max = matchResult.groupValues[2].toInt()
        val key = matchResult.groupValues[3]
        val pass = matchResult.groupValues[4]
//        println("$min | $max | $key | $pass")

        val occurrences = key.toRegex().findAll(pass).count()
        if (occurrences in min..max) validCounter++
    }

    println("\nThe number of valid passwords is $validCounter")
}