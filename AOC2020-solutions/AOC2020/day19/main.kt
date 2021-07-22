package day19

/*
Challenge at https://adventofcode.com/2020/day/19
 */

import java.io.BufferedReader
import java.io.File

class Rule(val id: Int, private val description: String, private val ruleMap: Map<Int, Rule>) {
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
    val input = getRulesAndTestsFromInput("in.txt")
    val ruleIdDescriptionMap = input.first
    val testList = input.second

    val ruleMap = HashMap<Int, Rule>()
    ruleIdDescriptionMap.forEach { (id, description) ->
        ruleMap[id] = Rule(id, description, ruleMap)
    }

    println(ruleMap[0]!!.pattern)
    val testMatchesCount = testList.filter { test ->
        test.matches(ruleMap[0]!!.pattern!!.toRegex())
    }.size

    println("$testMatchesCount tests match the rules.")
}

fun getRulesAndTestsFromInput(fileName: String): Pair<Map<Int, String>, List<String>> {
    val currentDir = System.getProperty("user.dir") + "/src/day19"
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

fun getTestsFromInput(buffReader: BufferedReader): List<String> {
    val testList = ArrayList<String>()
    buffReader.forEachLine { line ->
        testList.add(line)
    }
    return testList
}