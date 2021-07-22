package day4Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/4#part2

You can continue to ignore the cid field, but each other field has strict rules about
what values are valid for automatic validation:
    byr (Birth Year) - four digits; at least 1920 and at most 2002.
    iyr (Issue Year) - four digits; at least 2010 and at most 2020.
    eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
    hgt (Height) - a number followed by either cm or in:
        If cm, the number must be at least 150 and at most 193.
        If in, the number must be at least 59 and at most 76.
    hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
    ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
    pid (Passport ID) - a nine-digit number, including leading zeroes.
    cid (Country ID) - ignored, missing or not.
Count the number of valid passports - those that have all required fields
and valid values. Continue to treat cid as optional. In your batch file,
how many passports are valid?
 */

fun main() {
    val passports = readPassportsFromFile("in.txt")

    var validPassportCounter = 0
    passports.forEach {
        if (validateBirthYear(it) &&
            validateIssueYear(it) &&
            validateExpirationYear(it) &&
            validateHeight(it) &&
            validateHairColor(it) &&
            validateEyeColor(it) &&
            validatePassportId(it)
        )
            validPassportCounter++
    }
    println("The number of valid passports is $validPassportCounter")
}

fun readPassportsFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day4Part2"
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

fun validateBirthYear(pass: String): Boolean {
    return validateYear(pass, """\bbyr:(\d{4})\b""".toRegex(), 1920..2002)
}

fun validateIssueYear(pass: String): Boolean {
    return validateYear(pass, """\biyr:(\d{4})\b""".toRegex(), 2010..2020)
}

fun validateExpirationYear(pass: String): Boolean {
    return validateYear(pass, """\beyr:(\d{4})\b""".toRegex(), 2020..2030)
}

fun validateYear(pass: String, keyValueRegex: Regex, range: IntRange): Boolean {
    val matchResult = keyValueRegex.find(pass) ?: return false
    val value = matchResult.groupValues[1].toInt()
    return (value in range)
}

fun validateHeight(pass: String): Boolean {
    val keyValueRegex = """\bhgt:(\d*)(in|cm)\b""".toRegex()
    val matchResult = keyValueRegex.find(pass) ?: return false
    val value = matchResult.groupValues[1].toInt()
    val unit = matchResult.groupValues[2]
    return when (unit) {
        "cm" -> (value in 150..193)
        "in" -> (value in 59..76)
        else ->
            false
    }
}

fun validateHairColor(pass: String): Boolean {
    val keyValueRegex = """\bhcl:#[0-9 a-f]{6}\b""".toRegex()
    return pass.contains(keyValueRegex)
}

fun validateEyeColor(pass: String): Boolean {
    val keyValueRegex = """\becl:(amb|blu|brn|gry|grn|hzl|oth)\b""".toRegex()
    return pass.contains(keyValueRegex)
}

fun validatePassportId(pass: String): Boolean {
    val keyValueRegex = """\bpid:\d{9}\b""".toRegex()
    return pass.contains(keyValueRegex)
}
