package day13

import utils.let
import java.io.File

/*
Challenge at https://adventofcode.com/2020/day/13
 */

fun main() {
    val inputData = getScheduleFromFile("inExample.txt")
    val departureTimeFrom = inputData.first
    val busSchedule = inputData.second

    val departureTimeAndBus: Pair<Int, Int> = solveForDepartureTimeAndBus(departureTimeFrom, busSchedule)
    val departureTime = departureTimeAndBus.first
    val busId = departureTimeAndBus.second
    let(departureTime, busId) { (time, bus) ->
        val solution = (time - departureTimeFrom) * bus

        println("The earliest bus from $departureTimeFrom onward departs at $time, " +
                        "bus ID: $bus\n" +
                        "The solution is $solution")
    }
}

fun getScheduleFromFile(fileName: String): Pair<Int, List<String>> {
    val currentDir = System.getProperty("user.dir") + "/src/day13"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val earliestDeparture = buffReader.readLine().toInt()
    val busSchedule = buffReader.readLine().split(",")
    return Pair(earliestDeparture, busSchedule)
}

fun solveForDepartureTimeAndBus(earliestDeparture: Int, busSchedule: List<String>): Pair<Int, Int> {
    var timeOfDeparture = earliestDeparture
    val busScheduleFiltered = busSchedule.filter { it != "x" }
    while (true) {
        busScheduleFiltered.forEach { bus ->
            if (timeOfDeparture % bus.toInt() == 0) {
                return Pair(timeOfDeparture, bus.toInt())
            }
        }
        timeOfDeparture++
    }
}