package day23

/*
Challenge at https://adventofcode.com/2020/day/23
*/

import java.lang.IllegalStateException

class CircleOfCups(input: String) {
    private val cups = ArrayList<Int>()
    private var i: Int

    init {
        if (input == "") {
            throw IllegalStateException()
        }
        input.forEach { cups.add(Character.getNumericValue(it)) }
        i = 0
    }

    fun next(): Int {
        val cup = cups[i]
        if (i < cups.size - 1) {
            i++
        } else {
            i = 0
        }
        return cup
    }

    fun pull(): Int {
        val cup = cups[i]
        cups.removeAt(i)
        if (i > cups.size - 1) {
            i = 0
        }
        return cup
    }

    fun insert(cup: Int) = cups.add(++i, cup)

    // If this would select one of the cups that was just picked up, the crab will keep subtracting one until it finds a cup
    // that wasn't just picked up. If at any point in this process the value goes below the lowest value on any cup's label,
    // it wraps around to the highest value on any cup's label instead.
    fun setDestinationCup(dest: Int) {
        var d = dest
        while (!cups.contains(d)) {
            d--
            if (d < cups.minOrNull()!!) {
                d = cups.maxOrNull()!!
            }
        }
        i = cups.indexOf(d)
    }

    fun setCurrentAfter(current: Int) {
        i = cups.indexOf(current) + 1
        if (i > cups.size - 1) {
            i = 0
        }
    }

    override fun toString(): String = cups.foldIndexed("") { ie, acc, e -> if (ie == i) acc + "($e)" else acc + "$e"}
}

const val INPUT = "389125467"
//const val INPUT = "974618352"

fun main() {
    val circle = CircleOfCups(INPUT)
    println(circle)

    for (i in 1..10) {
        // The crab picks up the three cups that are immediately clockwise of the current cup.
        val currentCup = circle.next()
        val pickedCups = Array(3) { circle.pull() }
        // The crab selects a destination cup: the cup with a label equal to the current cup's label minus one.
        circle.setDestinationCup(currentCup - 1)
        // The crab places the cups it just picked up so that they are immediately clockwise of the destination cup.
        pickedCups.forEach { circle.insert(it) }
        circle.setCurrentAfter(currentCup)
        println("Round $i, order: $circle")
    }

    // After the crab is done, what order will the cups be in? Starting after the cup labeled 1, collect the other cups'
    // labels clockwise into a single string with no extra characters;
    val finalOrder = circle.toString().filter { it != '(' && it != ')' }
    val oneAt = finalOrder.indexOf('1')
    println("The solution is: ${finalOrder.slice(oneAt + 1 until finalOrder.length) + finalOrder.slice(0 until oneAt)}")
}

