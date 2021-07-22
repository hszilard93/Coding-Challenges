package day20

/*
Challenge at https://adventofcode.com/2020/day/20
 */

import java.io.File

class ImageTile(val id: String, val pixelArray: ArrayList<String>) {
    val neighbors = HashSet<ImageTile>()
    val borders: ArrayList<String>
        get() {
            val borders = ArrayList<String>()
            borders.add(pixelArray.first())
            var rightBorder = ""
            pixelArray.forEach { line ->
                rightBorder += line.last()
            }
            borders.add(rightBorder)
            borders.add(pixelArray.last())
            var leftBorder = ""
            pixelArray.forEach { line ->
                leftBorder += line.first()
            }
            borders.add(leftBorder)
            return borders
        }

    override fun toString(): String {
        var output = this.id + "\n"
        this.pixelArray.forEach { line -> output += line + "\n" }
        return output
    }
}

fun main() {
    val imageTiles = getImageTiles("in.txt")

    // We need to match the borders of the image tiles.
    for (i in imageTiles.indices) {
        for (j in i + 1 until imageTiles.size) {
            if (imageTiles[j] in imageTiles[i].neighbors) {
                continue
            }
            else {
                imageTiles[i].borders.forEach loop@{ border ->
                    imageTiles[j].borders.forEach { otherBorder ->
                        if (border == otherBorder || border == otherBorder.reversed()) {
                            imageTiles[i].neighbors.add(imageTiles[j])
                            imageTiles[j].neighbors.add(imageTiles[i])
                            return@loop // An image tile can only have a single shared border with another tile
                        }
                    }
                }
            }
        }
    }

    var result = 1L
    imageTiles.forEach { tile ->
        println(tile.id + " " + tile.neighbors.map { it.id })
        if (tile.neighbors.size == 2) {
            result *= tile.id.toInt()
        }
    }
    println(result)
}

fun getImageTiles(fileName: String): List<ImageTile> {
    val imageTiles = ArrayList<ImageTile>()

    val currentDir = System.getProperty("user.dir") + "/src/day20"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    var previousLine = ""
    lateinit var currentImageTile: ImageTile
    buffReader.lines().forEach { line ->
        // If previous line is empty, we start reading nem image tile, starting with its header
        if (previousLine == "") {
            val headerMatcher = """Tile (\d+):""".toRegex()
            val match = headerMatcher.find(line)
            val id = match!!.groupValues[1]
            currentImageTile = ImageTile(id, ArrayList())
        } else {
            // If line is empty, we have finished checking our array
            if (line == "") {
                imageTiles.add(currentImageTile)
            } else {
                currentImageTile.pixelArray.add(line)
            }
        }
        previousLine = line
    }
    imageTiles.add(currentImageTile)

    return imageTiles
}