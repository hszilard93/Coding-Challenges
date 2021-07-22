package day4

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/4

Passport data is validated in batch files (your puzzle input).
Each passport is represented as a sequence of key:value pairs separated by spaces or newlines.
Passports are separated by blank lines.
Count the number of valid passports - those that have all required fields.
Treat cid as optional. In your batch file, how many passports are valid?
 */

fun main() {
    val passports = readPassportsFromFile("in.txt")
//    passports.forEach { println(it) }
    var validPassports = 0

    val keys = arrayOf("byr:", "iyr:", "eyr:", "hgt", "hcl", "ecl", "pid")
    passports.forEach { pass ->
        var valid = true
        for (i in keys.indices) {
            if (!keys[i].toRegex().containsMatchIn(pass)) {
                valid = false
//                println("${keys[i]} not found in $pass")
                break
            }
        }
        if (valid) validPassports++
    }
    println("The number of valid passports is $validPassports")
}

fun readPassportsFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day4"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val passports = ArrayList<String>()
    var currentPassport = ""
    buffReader.forEachLine { line ->
        if (line.isBlank()) {
            passports.add(currentPassport)
            currentPassport = ""
        } else
            currentPassport += "$line\n"
    }
    passports.add(currentPassport)
    return passports
}