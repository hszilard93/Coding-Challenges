package day22Part2

/*
Challenge at https://adventofcode.com/2020/day/22#part2
 */

import java.io.File

class Deck(val owner: String) {
    private val deck = ArrayList<Int>()

    constructor(deck: Deck) : this(deck.owner) {
        deck.deck.forEach { this.deck.add(it) }
    }

    constructor(deck: Deck, numOfCards: Int) : this(deck.owner) {
        for (i in 0 until numOfCards) {
            this.deck.add(deck.deck[i])
        }
    }

    fun add(card: Int) = deck.add(card)

    fun peek(): Int = deck.first()

    fun pull(): Int {
        val card = deck.first()
        deck.removeFirst()
        return card
    }

    fun size(): Int = deck.size

    override fun hashCode(): Int = deck.hashCode() + owner.hashCode() * 10

    override fun toString(): String {
        return "$owner's deck: $deck"
    }
}

fun main() {
    val deckPair = getDecksFromFile("in.txt")
    deckPair.toList().forEach(::println)

    val winner = play(deckPair)
    println("The winning deck is $winner")
    println("The final score is ${determineScore(Deck(winner))}")
}

fun play(deckPair: Pair<Deck, Deck>): Deck {
    val previousRoundsLineups = HashSet<Pair<Int, Int>>()
    deckPair.apply {
        while (first.size() != 0 && second.size() != 0) {
            val lineup = Pair(first.hashCode(), second.hashCode())
            if (previousRoundsLineups.contains(lineup)) {
                return first
            }
            else {
                previousRoundsLineups.add(lineup)
            }
            val card1 = first.pull()
            val card2 = second.pull()
            if (first.size() >= card1 && second.size() >= card2) {
                val winnerOfSubGame = play(Pair(Deck(first, card1), Deck(second, card2)))
                if (winnerOfSubGame.owner == "Player 1") {
                    first.add(card1)
                    first.add(card2)
                }
                else {
                    second.add(card2)
                    second.add(card1)
                }
            }
            else {
                if (card1 > card2) {
                    first.add(card1)
                    first.add(card2)
                } else {
                    second.add(card2)
                    second.add(card1)
                }
            }
        }
    }
    return if (deckPair.first.size() == 0) deckPair.second else deckPair.first
}

fun determineScore(deck: Deck): Int {
    var score = 0
    for (i in deck.size() downTo 1) {
        score += deck.pull() * i
    }
    return score
}

fun getDecksFromFile(fileName: String): Pair<Deck, Deck> {
    val currentDir = System.getProperty("user.dir") + "/src/day22Part2"
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