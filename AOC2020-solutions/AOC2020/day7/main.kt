package day7

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/7

Bags must be color-coded and must contain specific quantities of other color-coded bags.
How many bag colors can eventually contain at least one shiny gold bag?
 */

class Bag(val name: String) {
    companion object { val allBags = HashMap<String, Bag>() }
    var parents = ArrayList<String>()
    var children = ArrayList<String>()

    fun addChild(childName: String) {
        children.add(childName)
        allBags.putIfAbsent(childName, Bag(childName))
        allBags[childName]!!.parents.add(this.name)
    }

    override fun toString(): String {
        var s = "$name has the following children and parents:\n"
        parents.forEach { s += "\tParent $it\n" }
        children.forEach { s += "\tChild $it\n" }
        return s
    }
}

fun main() {
    populateBagsFromFile("in.txt")
//    Bag.allBags.forEach { println(it.toString()) }
    val descendantBag = "shiny gold bag"
    val ancestors = HashSet<Bag>()
    collectAllAncestors(descendantBag, ancestors)
    println("This many other bags can eventually contain a $descendantBag: ${ancestors.size}")
}

fun collectAllAncestors(bagName: String, ancestors: HashSet<Bag>) {
    Bag.allBags.let { bags ->
        if (!ancestors.contains(bags[bagName])) {
            bags[bagName]!!.parents.forEach { parent ->
                collectAllAncestors(parent, ancestors)
                ancestors.add(bags[parent]!!)
            }
        }
    }
}

fun populateBagsFromFile(fileName: String) {
    val currentDir = System.getProperty("user.dir") + "/src/day7"
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
        val children = ArrayList<String>()
        childrenSeparated.forEach { child ->
            val childRegex = """(?:\d ([\w ]*bag))""".toRegex()
            val matchResult = childRegex.find(child)
            val childName = matchResult?.groupValues?.get(1)
            if (childName != null)
                children.add(childName)
        }

        Bag.allBags.run {
            putIfAbsent(bagName, Bag(bagName))
            children.forEach { childName ->
                get(bagName)!!.addChild(childName)
            }
        }
    }
}