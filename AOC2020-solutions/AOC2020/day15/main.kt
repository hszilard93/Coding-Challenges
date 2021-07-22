import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/15#part2
*/

//private val N = 2020
private val N = 30000000

fun main() {
    val startingNumbers = getStartingNumbersFromFile("in.txt")

    val history = HashMap<Int, Int>()
    for (i in 0 until startingNumbers.size - 1) {
        history[startingNumbers[i]] = i
    }
    var lastNumber = startingNumbers.last()
    for (turn in startingNumbers.size - 1 until N - 1) {
        val newNumber = if (history.contains(lastNumber)) turn - history[lastNumber]!! else 0
        history[lastNumber] = turn
        lastNumber = newNumber
    }
    println(lastNumber)
}

fun getStartingNumbersFromFile(fileName: String): List<Int> {
    val currentDir = System.getProperty("user.dir") + "/src/day15"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    return buffReader.readLine().split(',').map { it.toInt() }
}
