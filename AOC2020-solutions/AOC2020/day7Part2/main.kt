package day7Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/7#part2

Bags must be color-coded and must contain specific quantities of other color-coded bags.
How many bag colors can eventually contain at least one shiny gold bag?
 */

class Bag(val name: String) {
    companion object { val allBags = HashMap<String, Bag>() }
    var parents = ArrayList<String>()
    var children = ArrayList<Pair<String, Int>>()

    fun addChild(childName: String, count: Int) {
        children.add(Pair(childName, count))
        allBags.putIfAbsent(childName, Bag(childName))
        allBags[childName]!!.parents.add(this.name)
    }

    override fun toString(): String {
        var s = "$name has the following children and parents:\n"
        parents.forEach { s += "\tParent $it\n" }
        children.forEach { s += "\tChild ${it.second} ${it.first}\n" }
        return s
    }
}

fun main() {
    populateBagsFromFile("in.txt")
//    Bag.allBags.forEach { println(it.toString()) }
    val outermostBag = "shiny gold bag"
    val innerBagsCount = countAllBagsContained(outermostBag) - 1

    println("A $outermostBag contains this many other bags: $innerBagsCount")
}

fun countAllBagsContained(outerName: String): Int {
    var outerCounter = 1
    Bag.allBags.let { bags ->
        bags[outerName]!!.children.forEach { child ->
            val innerCounter = countAllBagsContained(child.first)
            outerCounter += innerCounter * child.second
        }
    }
    return outerCounter
}

fun populateBagsFromFile(fileName: String) {
    val currentDir = System.getProperty("user.dir") + "/src/day7Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    buffReader.forEachLine { line: String ->
        val bagAndItsChildrenRegex =
            """\b([\w\s]*bag)s contain ((?:\d(?:(?:[\w ]*[bag|s])[,|.])*)|no other bags.)""".toRegex()
        val matchResult = bagAndItsChildrenRegex.find(line)
        val bagName: String
        val childrenAll: String
        if (matchResult != null) {
            bagName = matchResult.groupValues[1]
            childrenAll = matchResult.groupValues[2]
        } else
            throw Exception("Invalid input: $line")
//        println("Bag: $bagName | children: $childrenAll")

        val childrenSeparated = childrenAll.split(',')
        val children = ArrayList<Pair<String, Int>>()
        childrenSeparated.forEach { child ->
            val childRegex = """(?:(\d+ [\w ]*bag))""".toRegex()
            val matchResult = childRegex.find(child)
            val childCountAndName = matchResult?.groupValues?.get(1)
            if (childCountAndName != null) {
                val childCountRegex = """(\d+) ([\w ]*)""".toRegex()
                val matchResult = childCountRegex.find(childCountAndName)
                val childCount = matchResult!!.groupValues[1].toInt()
                val childName = matchResult.groupValues[2]
//                println("$childCount $childName")
                children.add(Pair(childName, childCount))
            }
        }

        Bag.allBags.run {
            putIfAbsent(bagName, Bag(bagName))
            children.forEach { child ->
                get(bagName)!!.addChild(child.first, child.second)
            }
        }
    }
}   