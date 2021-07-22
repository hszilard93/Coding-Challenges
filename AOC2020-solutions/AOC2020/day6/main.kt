package day6

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/6

For each group, count the number of questions to which anyone answered "yes".
What is the sum of those counts?
 */

fun main() {
    val answersFromGroups = readAnswersFromFile("in.txt")

    var sumOfAnswersByGroup = 0
    answersFromGroups.forEach { answers ->
//        println(it)
        sumOfAnswersByGroup += answers.toCharArray().distinct().size
    }
    println("The sum of the number of questions each group answered: $sumOfAnswersByGroup")
}

fun readAnswersFromFile(fileName: String): ArrayList<String> {
    val currentDir = System.getProperty("user.dir") + "/src/day6"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val answersFromGroups = ArrayList<String>()
    var currentGroupAnswers = ""
    buffReader.forEachLine {
        if (!it.isBlank())
            currentGroupAnswers += it
        else {
            answersFromGroups.add(currentGroupAnswers)
            currentGroupAnswers = ""
        }
    }
    answersFromGroups.add(currentGroupAnswers)
    return answersFromGroups
}
