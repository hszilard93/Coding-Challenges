package day25

/*
Challenge at https://adventofcode.com/2020/day/25
 */

import java.io.File

fun main() {
    val cardSubjectNumber = 7
    val doorSubjectNumber = 7
    val keyPair = getPublicKeysFromFile("in.txt")
    val cardPublicKey = keyPair.first
    val doorPublicKey = keyPair.second

    var cardValue = 1
    var cardLoopSize = 0
    while (cardValue != cardPublicKey) {
        cardValue *= cardSubjectNumber
        cardValue %= 20201227
        cardLoopSize += 1
    }
    var doorValue = 1
    var doorLoopSize = 0
    while (doorValue != doorPublicKey) {
        doorValue *= doorSubjectNumber
        doorValue %= 20201227
        doorLoopSize += 1
    }

    var encryptionKey = 1L
    for (i in 1..cardLoopSize) {
        encryptionKey *= doorPublicKey
        encryptionKey %= 20201227
    }

    println("The secret card loop size is $cardLoopSize")
    println("The secret door loop size is $doorLoopSize")
    println("The encryption key is $encryptionKey")
}

fun getPublicKeysFromFile(fileName: String): Pair<Int, Int> {
    val currentDir = System.getProperty("user.dir") + "/src/day25"
    val buffReader = File("$currentDir/$fileName").bufferedReader()
    val cardKey = buffReader.readLine().toInt()
    val doorKey = buffReader.readLine().toInt()
    return Pair(cardKey, doorKey)
}