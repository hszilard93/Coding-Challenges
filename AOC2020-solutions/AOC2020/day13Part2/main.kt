package day13Part2

import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/13#part2
 */

fun main() {
    val inputData = getScheduleFromFile("in.txt")
    val busSchedule = inputData.second

    val firstCommonDepartureWithInterval = findCommonDepartureWithIntervals(busSchedule)
    println("The buses will depart at the designated intervals at the earliest time of: $firstCommonDepartureWithInterval")
}

// The coinciding occurrences of any two or more regular schedules will repeat at regular intervals.
fun findCommonDepartureWithIntervals(busSchedule: List<Int?>): Long {
    val busesAndRequiredIntervals = ArrayList<Pair<Int, Int>>()
    busSchedule.forEachIndexed { i, busId ->
        if (busId != null) {
            busesAndRequiredIntervals.add(Pair(busId, i))
        }
    }

    var interval = busesAndRequiredIntervals.first().first.toLong()
    busesAndRequiredIntervals.removeAt(0)
    var time = 0L
    busesAndRequiredIntervals.forEach { (busId, delay) ->
        var firstCommonOccurrence: Long? = null
        while (true) {
            if ((time + delay) % busId == 0L) {
                println(time)
                if (firstCommonOccurrence == null) {
                    firstCommonOccurrence = time
                }
                else {
                    interval = time - firstCommonOccurrence
                    break
                }
            }
            time += interval
        }
    }

    return time - interval
}

fun getScheduleFromFile(fileName: String): Pair<Int, List<Int?>> {
    val currentDir = System.getProperty("user.dir") + "/src/day13Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val earliestDeparture = buffReader.readLine().toInt()
    val busSchedule = buffReader.readLine().split(",").map { if (it == "x") null else it.toInt() }
    return Pair(earliestDeparture, busSchedule)
}
