package day6Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/6#part2

For each group, count the number of questions to which anyone answered "yes".
What is the sum of those counts?
 */

fun main() {
    val answersFromGroups = readAnswersFromFile("in.txt")

    var sumOfAnswersByAllFromGroups = 0
    answersFromGroups.forEach { answersByGroup ->
        val occurrences = HashMap<Char, Int>()
        answersByGroup.forEach { answersByInduvidual ->
            answersByInduvidual.forEach { answer: Char ->
                if (occurrences.containsKey(answer))
                    occurrences[answer] = occurrences[answer]!! + 1
                else
                    occurrences[answer] = 1
            }
        }
        var answeredByAllCounter = 0
//        print("\nThese questions were answered by all in group: ")
        occurrences.forEach {
            if (it.value == answersByGroup.size) {
//                print("${it.key}")
                answeredByAllCounter++
            }
        }
        sumOfAnswersByAllFromGroups += answeredByAllCounter
    }
    println("\nThe sum of the number of questions answered by all in each group is $sumOfAnswersByAllFromGroups")
}

fun readAnswersFromFile(fileName: String): ArrayList<ArrayList<String>> {
    val currentDir = System.getProperty("user.dir") + "/src/day6Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val answersFromAllGroups = ArrayList<ArrayList<String>>()
    var currentGroupAnswers = ArrayList<String>()
    buffReader.forEachLine {
        if (it.isNotBlank())
            currentGroupAnswers.add(it)
        else {
            answersFromAllGroups.add(currentGroupAnswers)
            currentGroupAnswers = ArrayList()
        }
    }
    answersFromAllGroups.add(currentGroupAnswers)

    return answersFromAllGroups
}
