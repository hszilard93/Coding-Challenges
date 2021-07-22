package day19Part2

/*
Challenge at https://adventofcode.com/2020/day/19#part2
 */

import java.io.BufferedReader
import java.io.File

class Rule(val id: Int, val description: String, private val ruleMap: Map<Int, Rule>) {
    var pattern: String? = null
        get() {
            if (field == null) {
                parseDescriptionToIntermediateForm()
            }
            return field
        }

    private fun parseDescriptionToIntermediateForm() {
        val simplePattern = """"(\w)"""".toRegex().find(description)?.groupValues?.get(1)
        if (simplePattern != null) {
            pattern = simplePattern
        }
        else {
            pattern = "("
            val options = description.split("|")
            options.forEach { ids ->
                val rules = """(\d+)""".toRegex().findAll(ids)
                var optionPattern = ""
                rules.map { it.value.toInt() }.forEach { id ->
                    optionPattern += ruleMap[id]!!.pattern
                }
                pattern += if (pattern!!.length > 1) "|$optionPattern" else optionPattern
            }
            pattern += ")"
        }
    }
}

fun main() {
    val input = getRulesAndTestsFromInput("inExample.txt")
    val ruleIdDescriptionMap = input.first
    val testList = input.second

    val ruleMap = HashMap<Int, Rule>()
    ruleIdDescriptionMap.forEach { (id, description) ->
        ruleMap[id] = Rule(id, description, ruleMap)
    }

    ruleMap[8] = Rule(8, "42 | 42 8", ruleMap)
    ruleMap[11] = Rule(11, "42 31 | 42 11 31", ruleMap)
    val testMatchesCount = findMatchingTestCountRecursively(ruleMap, testList)

    println("$testMatchesCount tests match the rules.")
}

// This should work, but it doesn't, and I can't figure it out. :(
fun findMatchingTestCountRecursively(ruleMap: MutableMap<Int, Rule>, tests: MutableList<String>): Int {
//    var matchCount = 0

    val rule8Description = "42 | (42 8)"
    val rule11Description = "42 31 | (42 11 31)"

    var rule8D2 = rule8Description
    for (i in 1..20) {
        rule8D2 = rule8D2.replace("8", "(42 | (42 8))")
    }
    rule8D2 = rule8D2.replace("8", "")
    var rule11D2 = rule11Description
    for (j in 1..20) {
        rule11D2 = rule11D2.replace("11", "(42 31 | (42 11 31))")
    }
    rule11D2 = rule11D2.replace("11", "")
    ruleMap[8] = Rule(8, rule8D2, ruleMap)
    ruleMap[11] = Rule(11, rule11D2, ruleMap)
    println(ruleMap[8]?.pattern)
    println(ruleMap[11]?.pattern)
//    ruleMap[0] = Rule(ruleMap[0]!!.id, ruleMap[0]!!.description, ruleMap)
    val matchCount = tests.filter { test ->
        test.matches(ruleMap[0]!!.pattern!!.toRegex())
    }.size

//    for (i in 0..20) {
//        var rule8D2 = rule8Description
//        for (j in 1..i) {
//            rule8D2 = rule8D2.replace("8", "(42 | 42 8)")
//        }
//        rule8D2 = rule8D2.replace("8", "")
//        var rule11D2 = rule11Description
//        for (j in 1..i) {
//            rule11D2 = rule11D2.replace("11", "(42 31 | 42 11 31)")
//        }
//        rule11D2 = rule11D2.replace("11", "")
//
//        ruleMap[8] = Rule(8, rule8D2, ruleMap)
//        ruleMap[11] = Rule(11, rule11D2, ruleMap)
//        ruleMap[0] = Rule(ruleMap[0]!!.id, ruleMap[0]!!.description, ruleMap)
//
////        println("$i: " + ruleMap[0]!!.pattern)
//
//        val matchingTests = tests.filter { test ->
//            test.matches(ruleMap[0]!!.pattern!!.toRegex())
//        }
//        matchCount += matchingTests.size
//        tests.removeAll(matchingTests)
//    }

    return matchCount
}

fun getRulesAndTestsFromInput(fileName: String): Pair<Map<Int, String>, MutableList<String>> {
    val currentDir = System.getProperty("user.dir") + "/src/day19Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()
    val ruleMap = getRulesFromInput(buffReader)
    buffReader.reset()
    val testList = getTestsFromInput(buffReader)
    return Pair(ruleMap, testList)
}

fun getRulesFromInput(buffReader: BufferedReader): Map<Int, String> {
    val ruleMap = HashMap<Int, String>()
    while(true) {
        buffReader.mark(1000)
        val line =buffReader.readLine()
        if (line.isBlank()) {
            break
        }
        val pattern = """^(\d+):(.*)$""".toRegex()
        val matchResult = pattern.find(line) ?: break
        val id = matchResult.groupValues[1].toInt()
        val ruleDescription = matchResult.groupValues[2].trim()
        ruleMap[id] = ruleDescription
    }
    return ruleMap
}

fun getTestsFromInput(buffReader: BufferedReader): MutableList<String> {
    val testList = ArrayList<String>()
    buffReader.forEachLine { line ->
        testList.add(line)
    }
    return testList
}