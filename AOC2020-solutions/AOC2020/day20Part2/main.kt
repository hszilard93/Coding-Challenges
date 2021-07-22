package day20Part2

/*
Challenge at https://adventofcode.com/2020/day/20#part2
This was a bad'un!
 */

import java.io.File
import java.lang.IllegalStateException
import kotlin.math.sqrt

fun ArrayList<CharArray>.rotateClockwise() {
    if (this.size != this[0].size) {
        throw IllegalStateException("Not a square matrix! ${this.size} != ${this[0].size}")
    }

    val newArray = ArrayList<CharArray>()
    val n = this.size
    for (i in 0 until n) {
        newArray.add(CharArray(n))
    }
    for (i in 0 until n) {
        for (j in 0 until n) {
            newArray[j][n - i - 1] = this[i][j]
        }
    }
    for (i in this.indices) {
        this[i] = newArray[i]
    }
}

fun ArrayList<CharArray>.flipVertically() {
    if (this.size != this[0].size) {
        throw IllegalStateException("Not a square matrix! ${this.size} != ${this[0].size}")
    }

    val newArray = ArrayList<CharArray>()
    val n = this.size
    for (i in 1..n) {
        newArray.add(CharArray(n))
    }
    for (i in 0 until n) {
        for (j in 0 until n) {
            newArray[i][n - j - 1] = this[i][j]
        }
    }
    for (i in this.indices) {
        this[i] = newArray[i]
    }
}

fun ArrayList<CharArray>.flipHorizontally() {
    if (this.size != this[0].size) {
        throw IllegalStateException("Not a square matrix! ${this.size} != ${this[0].size}")
    }

    val newArray = ArrayList<CharArray>()
    val n = this.size
    for (i in 1..n) {
        newArray.add(CharArray(n))
    }
    for (i in 0 until n) {
        for (j in 0 until n) {
            newArray[n - i - 1][j] = this[i][j]
        }
    }
    for (i in this.indices) {
        this[i] = newArray[i]
    }
}

fun ArrayList<CharArray>.toBetterString(): String {
    var output = ""
//    (0 until size).forEach { i -> output += "$i " }
//    output += "\n"
    var l = 0
    forEach { line -> output += l++.toString().padStart(3) + " " + line.joinToString("") + "\n" }
    return output
}

class ImageTile(val id: String, val pixelArray: ArrayList<CharArray>) {
    val neighbors = HashSet<ImageTile>()

    fun borders(): ArrayList<String> {
        val borders = ArrayList<String>()
        borders.add(pixelArray.first().joinToString(""))
        val rightBorder = pixelArray.map { it.last() }.joinToString("")
        borders.add(rightBorder)
        borders.add(pixelArray.last().joinToString(""))
        val leftBorder = pixelArray.map { it.first() }.joinToString("")
        borders.add(leftBorder)
        return borders
    }

    fun upperBorder(): String = pixelArray.first().joinToString("")

    fun leftBorder(): String = pixelArray.map { it.first() }.joinToString("")

    fun bottomBorder(): String = pixelArray.last().joinToString("")

    fun rightBorder(): String = pixelArray.map { it.last() }.joinToString("")

    fun rotateClockwise() = pixelArray.rotateClockwise()

    fun flipVertically() = pixelArray.flipVertically()

    fun flipHorizontally() = pixelArray.flipHorizontally()

    override fun toString(): String = this.id + "\n" + pixelArray.toBetterString()
}

class Image(val pixelArray: ArrayList<CharArray>) {

    fun rotateClockwise() = pixelArray.rotateClockwise()

    fun flipVertically() = pixelArray.flipVertically()

    fun flipHorizontally() = pixelArray.flipHorizontally()

    override fun toString(): String = pixelArray.toBetterString()
}

fun getImageTiles(fileName: String): List<ImageTile> {
    val imageTiles = ArrayList<ImageTile>()

    val currentDir = System.getProperty("user.dir") + "/src/day20Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    var previousLine = ""
    lateinit var currentImageTile: ImageTile
    buffReader.lines().forEach { line ->
        // If previous line is empty, we start reading nem image tile, starting with its header
        if (previousLine == "") {
            val headerRegex = """Tile (\d+):""".toRegex()
            val match = headerRegex.find(line)
            val id = match!!.groupValues[1]
            currentImageTile = ImageTile(id, ArrayList())
        } else {
            // If line is empty, we have finished checking our array
            if (line == "") {
                imageTiles.add(currentImageTile)
            } else {
                currentImageTile.pixelArray.add(line.toCharArray())
            }
        }
        previousLine = line
    }
    imageTiles.add(currentImageTile)

    return imageTiles
}

fun main() {
    val imageTiles = getImageTiles("in.txt")

    // We need to match the borders of the image tiles.
    for (i in imageTiles.indices) {
        for (j in i + 1 until imageTiles.size) {
            if (imageTiles[j] in imageTiles[i].neighbors) {
                continue
            } else {
                imageTiles[i].borders().forEach loop@{ border ->
                    if (imageTiles[j].borders().any { it == border }) {
                        imageTiles[i].neighbors.add(imageTiles[j])
                        imageTiles[j].neighbors.add(imageTiles[i])
                        return@loop
                    }
                    imageTiles[j].flipVertically()
                    if (imageTiles[j].borders().any { it == border }) {
                        imageTiles[i].neighbors.add(imageTiles[j])
                        imageTiles[j].neighbors.add(imageTiles[i])
                        return@loop
                    }
                    imageTiles[j].flipHorizontally()
                    if (imageTiles[j].borders().any { it == border }) {
                        imageTiles[i].neighbors.add(imageTiles[j])
                        imageTiles[j].neighbors.add(imageTiles[i])
                        return@loop
                    }
                    imageTiles[j].flipVertically()
                    if (imageTiles[j].borders().any { it == border }) {
                        imageTiles[i].neighbors.add(imageTiles[j])
                        imageTiles[j].neighbors.add(imageTiles[i])
                        return@loop
                    }
                    imageTiles[j].flipHorizontally()
                }
            }
        }
    }

    val n = sqrt(imageTiles.size.toDouble()).toInt()
    val tileMatrix = Array(n) { Array<ImageTile?>(n) { null } }

    // We arbitrarily select a corner and make it the upper left corner to orient the other tiles by
    val upperLeftCorner = imageTiles.first { it.neighbors.size == 2 }
    tileMatrix[0][0] = upperLeftCorner
    val membersOfTileMatrix = HashSet<ImageTile>()
    membersOfTileMatrix.add(upperLeftCorner)
    // We fill up the first row
    for (j in 0 until n - 1) {
        tileMatrix[0][j + 1] = tileMatrix[0][j]!!.neighbors.first {
            it !in membersOfTileMatrix
                    && (it.neighbors.size == 2 || it.neighbors.size == 3)
        }
        membersOfTileMatrix.add(tileMatrix[0][j + 1]!!)
    }
    // We fill up the first column
    for (i in 0 until n - 1) {
        tileMatrix[i + 1][0] = tileMatrix[i][0]!!.neighbors.first {
            it !in membersOfTileMatrix
                    && (it.neighbors.size == 2 || it.neighbors.size == 3)
        }
        membersOfTileMatrix.add(tileMatrix[i + 1][0]!!)
    }

    // Now we have enough information to fill the entire matrix
    for (i in 1 until n) {
        for (j in 1 until n) {
            tileMatrix[i][j] = imageTiles.first {
                tileMatrix[i][j - 1] in it.neighbors
                        && tileMatrix[i - 1][j] in it.neighbors
                        && it !in membersOfTileMatrix
            }
            membersOfTileMatrix.add(tileMatrix[i][j]!!)
        }
    }

    for (i in 0 until n) {
        for (j in 0 until n) {
            print((tileMatrix[i][j]?.id ?: "NULL") + " ")
        }
        println()
    }

// Now we must rotate each image so their borders fit together
    for (i in 0 until n) {
        for (j in 0 until n) {
            tileMatrix[i][j]!!.let { tile ->
                println("$i $j")
                // Let's handle the 'corner' cases first
                when {
                    i == 0 && j == 0 -> {           // Top left corner
                        while (tile.rightBorder() !in (tileMatrix[i][j + 1]!!.borders()
                                        + tileMatrix[i][j + 1]!!.borders().map { it.reversed() })
                                || tile.bottomBorder() !in (tileMatrix[i + 1][j]!!.borders()
                                        + tileMatrix[i + 1][j]!!.borders().map { it.reversed() })) {
                            tile.rotateClockwise()
                        }
                    }
                    i == 0 && j == n - 1 -> {       // Top right corner
                        var r = 0
                        while (tile.leftBorder() != tileMatrix[i][j - 1]!!.rightBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    i == n - 1 && j == 0 -> {       // Bottom left corner
                        var r = 0
                        while (tile.upperBorder() != tileMatrix[i - 1][j]!!.bottomBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    i == n - 1 && j == n - 1 -> {   // Bottom right corner
                        var r = 0
                        while (tile.leftBorder() != tileMatrix[i][j - 1]!!.rightBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    i == 0 -> {                     // Top row
                        var r = 0
                        while (tile.leftBorder() != tileMatrix[i][j - 1]!!.rightBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    i == n - 1 -> {                 // Bottom row
                        var r = 0
                        while (tile.leftBorder() != tileMatrix[i][j - 1]!!.rightBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    j == 0 -> {                     // Leftmost column
                        var r = 0
                        while (tile.upperBorder() != tileMatrix[i - 1][j]!!.bottomBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    j == n - 1 -> {                 // Rightmost column
                        var r = 0
                        while (tile.leftBorder() != tileMatrix[i][j - 1]!!.rightBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                    else -> {                       // Tile in middle
                        var r = 0
                        while (tile.upperBorder() != tileMatrix[i - 1][j]!!.bottomBorder()
                                || tile.leftBorder() != tileMatrix[i][j - 1]!!.rightBorder()) {
                            tile.rotateClockwise()
                            r++
                            if (r == 4) {
                                tile.flipVertically()
                            }
                            if (r == 8) {
                                tile.flipHorizontally()
                            }
                            if (r == 12) {
                                tile.flipVertically()
                            }
                            if (r == 16) {
                                throw IllegalStateException("$i $j")
                            }
                        }
                    }
                }
            }
        }
    }

    println("Assembling image!")

    // Now that the tiles are correctly rotated, we must remove their borders and assemble them into a final image
    val imageArray = ArrayList<String>()
    for (i in tileMatrix.indices) {
        for (lineI in 1 until tileMatrix[i][0]!!.pixelArray.size - 1) {
            var imageRow = ""
            tileMatrix[i].forEach { tile ->
                tile!!.pixelArray[lineI].apply {
                    imageRow += joinToString("").substring(1, size - 1)
                }
            }
            imageArray.add(imageRow)
        }
    }
    val image = Image(ArrayList(imageArray.map { it.toCharArray() }))
    println(image)

    // We must search for any monsters in the sea
    val seaMonsterLine1RegEx = """..................#.""".toRegex()
    val seaMonsterLine2RegEx = """#....##....##....###""".toRegex()
    val seaMonsterLine3RegEx = """.#..#..#..#..#..#...""".toRegex()

    val detectedSeaMonsters = HashSet<Pair<Int, Int>>()
    var r = 0
    while (true) {
        image.pixelArray.let { array ->
            for (i in 1 until array.size - 1) {
                val line2 = array[i].joinToString("")
                val matchResults = seaMonsterLine2RegEx.findAll(line2)  // Searching for the 2nd line because it has fewer occurrences (also because stupid regex don't work!!)
                matchResults.forEach { result ->
                    val line1SubString = array[i - 1].joinToString("").substring(result.range)
                    val matchResultL1 = seaMonsterLine1RegEx.find(line1SubString)
                    val line3SubString = array[i + 1].joinToString("").substring(result.range)
                    val matchResultL3 = seaMonsterLine3RegEx.find(line3SubString)
                    if (matchResultL1 != null && matchResultL3 != null) {
                        detectedSeaMonsters.add(Pair(i - 1, result.range.first))
                    }
                }
            }
        }
        if (detectedSeaMonsters.isNotEmpty()) {
            break
        } else {
            image.rotateClockwise()
            r++
            if (r == 4) {
                image.flipVertically()
            }
            if (r == 8) {
                image.flipHorizontally()
            }
            if (r == 12) {
                image.flipVertically()
            }
            if (r == 16) {
                throw IllegalStateException("Monsters not found!")
            }
            println()
            println(image)
        }
    }
//    println(detectedSeaMonsters)

    // Now that we have spotted our monsters, we just have to measure the roughness of the sea (minus monsters)!
    var objectsInWater = 0
    image.pixelArray.let { array ->
        for (i in array.indices) {
            for (j in array[0].indices) {
                if (array[i][j] == '#') {
                    objectsInWater += 1
                }
            }
        }
    }
    println("The roughness of the sea is ${objectsInWater - (detectedSeaMonsters.size * 15)}")
    // Done! Phew.
}