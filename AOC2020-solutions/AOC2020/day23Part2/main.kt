package day23Part2

/*
Challenge at https://adventofcode.com/2020/day/23
*/

import java.util.*
import kotlin.system.measureNanoTime

const val SIZE_OF_CIRCLE = 1_000_000
const val ROUNDS = 10_000_000

//data class Cup(val value: Int, var nextValue: Int)

/* In this data structure, each cup is represented by it's position in the array (cup[0] represents the 1st cup,
 cup[999] represents the 1000th cup, and so on). The value of each cup represents its neighbour on the right.
 If cup[0]'s value is 10, it means that the cup to it's right is the cup numbered 11.
 In this way, pulling, insertion and lookup operations will always be done in O(1) time.
 I have not thought of using this data structure and only got the idea from a reddit thread. Don't tell! */
class CircleOfCups2(input: String) {
    private val cups = Array(SIZE_OF_CIRCLE) { i -> i }
    private var picked = ArrayList<Int>()
    private var current = input[0].digitToInt()

    init {
        for (i in 0 until input.length - 1) {
            cups[input[i].digitToInt() - 1] = input[i + 1].digitToInt()
        }

//        cups[input.last().digitToInt() - 1] = input[0].digitToInt()

        cups[input.last().digitToInt() - 1] = input.length + 1
        for (i in input.length until SIZE_OF_CIRCLE - 1) {
            cups[i] = i + 2
        }
        cups[SIZE_OF_CIRCLE - 1] = input[0].digitToInt()
    }

    fun current(): Int = current

    fun next() {
        current = cups[current - 1]
    }

    fun pull(): Int {
        val pick = cups[current - 1]
        picked.add(pick)
        cups[current - 1] = cups[pick - 1]
        return pick
    }

    fun insert(cup: Int) {
        val next = cups[current - 1]
        cups[current - 1] = cup
        cups[cup - 1] = next
        picked.remove(cup)
        current = cup
    }

    fun setDestinationCup(dest: Int) {
        var d = dest
        if (d < 1) {
            d = SIZE_OF_CIRCLE
        }
        while (d in picked) {
            d--
            if (d < 1) {
                d = SIZE_OF_CIRCLE
            }
        }
        current = d
    }

    fun advanceCurrentFrom(previous: Int) {
        current = cups[previous - 1]
    }

    //    override fun toString(): String = cups.foldIndexed("") {ie, acc, e -> acc + "$ie -> $e; "}
    override fun toString(): String {
        var acc = ""
        var next = 1
        var i = 0
        do {
            acc += if (next == current) "($next); " else "$next; "
            next = cups[next - 1]
            i++
        } while (next != 1 && i < 100)
        return acc
    }
}

//const val INPUT = "389125467"
const val INPUT = "974618352"

fun main() {
    val circle = CircleOfCups2(INPUT)
    val timeToSolve = measureNanoTime {
        for (i in 1..ROUNDS) {
//            println("Round $i, order: $circle")
            // The crab picks up the three cups that are immediately clockwise of the current cup.
            val currentCup = circle.current()
            val pickedCups = Array(3) { circle.pull() }
//            println("pick up: ${pickedCups.joinToString(" ")}")
            // The crab selects a destination cup: the cup with a label equal to the current cup's label minus one.
            circle.setDestinationCup(currentCup - 1)
            // The crab places the cups it just picked up so that they are immediately clockwise of the destination cup.
            pickedCups.forEach { circle.insert(it) }
            circle.advanceCurrentFrom(currentCup)
        }
    }

    // Determine which two cups will end up immediately clockwise of cup 1.
    // What do you get if you multiply their labels together?
    println("The final order is: $circle")
    println("Elapsed time: ${timeToSolve / 1_000_000_000.0} seconds")
}