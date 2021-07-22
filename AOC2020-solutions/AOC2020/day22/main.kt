package day22

/*
Challenge at https://adventofcode.com/2020/day/22
 */

import java.io.File

class Deck(val owner: String) {
    private val deck = ArrayList<Int>()

    fun add(card: Int) = deck.add(card)

    fun peek(): Int = deck.first()

    fun pull(): Int {
        val card = deck.first()
        deck.removeFirst()
        return card
    }

    fun size(): Int = deck.size

    override fun toString(): String {
        return "$owner's deck: $deck"
    }
}

fun main() {
    val deckPair = getDecksFromFile("in.txt")
    deckPair.toList().forEach(::println)

    deckPair.apply {
        while (first.size() != 0 && second.size() != 0) {
            val card1 = first.pull()
            val card2 = second.pull()
            if (card1 > card2) {
                first.add(card1)
                first.add(card2)
            }
            else {
                second.add(card2)
                second.add(card1)
            }
        }
    }
    val winner = if (deckPair.first.size() == 0) deckPair.second else deckPair.first
    println("The winning deck is $winner")
    var score = 0
    for (i in winner.size() downTo 1) {
        score += winner.pull() * i
    }
    println("The final score is $score")
}

fun getDecksFromFile(fileName: String): Pair<Deck, Deck> {
    val currentDir = System.getProperty("user.dir") + "/src/day22"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val deckPair = Pair(Deck("Player 1"), Deck("Player 2"))
    var currentDeck = deckPair.first
    buffReader.lines().forEach { line ->
        if (line.matches("""Player 2:""".toRegex())) {
            currentDeck = deckPair.second
        }
        else if (line.matches("""\d+""".toRegex())) {
            currentDeck.add(line.toInt())
        }
    }
    return deckPair
}