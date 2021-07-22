package day21Part2

/*
Challenge at https://adventofcode.com/2020/day/21#part2
 */

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val ingredientsAllergensList = getIngredientsAndAllergensFromInput("in.txt")
    val ingredientsSet = HashSet<String>()  // we need to keep a score of all our ingredients for this task

    val allergensIngredientsMap = HashMap<String, MutableSet<String>>()
    ingredientsAllergensList.forEach { ingredientsAllergensPair ->
    //        println("${it.first} - ${it.second}")
        ingredientsAllergensPair.first.forEach { ingredientsSet.add(it) }
        // for each allergen, we try to eliminate impossible counterparts as we go
        ingredientsAllergensPair.second.forEach { allergen ->
            if (allergen !in allergensIngredientsMap) {
                allergensIngredientsMap[allergen] = HashSet()
                ingredientsAllergensPair.first.forEach { ingredient ->
                    allergensIngredientsMap[allergen]!!.add(ingredient)
                }
            } else {
                allergensIngredientsMap[allergen]!!.apply {
                    val badList = ArrayList<String>()
                    forEach { existingIngredient ->
                        if (existingIngredient !in ingredientsAllergensPair.first) {
                            badList.add(existingIngredient)
                        }
                    }
                    removeIf { ingredient -> ingredient in badList }
                }
            }
        }
    }

    var onlySinglesLeft = false
    while (!onlySinglesLeft) {
        onlySinglesLeft = true
        // We need to find the allergens which already have a matching ingredient, and since there is a 1-1 relation between
        // allergens and ingredients, we can remove these ingredients from the other allergens' suspicious ingredients list.
        // Eventually, we must arrive to a state where there are only single allergen-ingredient pairs left, if the problem
        // set is correct.
        allergensIngredientsMap.forEach { allergenEntry ->
            if (allergenEntry.value.size == 1) {
                allergenEntry.value.first().let { matchedIngredient ->
                    allergensIngredientsMap.filterKeys { it != allergenEntry.key }.forEach { otherEntry ->
                        otherEntry.value.remove(matchedIngredient)
                        if (otherEntry.value.isEmpty()) {
                            throw IllegalStateException("Allergen empty: ${otherEntry.key}")
                        }
                    }
                }
            }
            else {
                onlySinglesLeft = false
            }
        }
    }

    val alphabeticalListOfAllergens = allergensIngredientsMap.keys.sorted()
    alphabeticalListOfAllergens.forEach { allergen ->
        print(allergensIngredientsMap[allergen]!!.first() + if(allergen == alphabeticalListOfAllergens.last()) "" else ",")
    }
//    println(allergensIngredientsMap)
}

fun getIngredientsAndAllergensFromInput(fileName: String): List<Pair<List<String>, List<String>>> {
    val ingredientsAndAllergens = ArrayList<Pair<List<String>, List<String>>>()

    val currentDir = System.getProperty("user.dir") + "/src/day21Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    buffReader.lines().forEach { line ->
        val ingredients = ArrayList<String>()
        val allergens = ArrayList<String>()

        val allergensPattern = """\(contains\s([\w,\s]+)\)""".toRegex()
        val match = allergensPattern.findAll(line)
        if (match.any()) {
            match.first().groupValues[1].let { allergensString ->
                val individualAllergenPattern = """(\w+)""".toRegex()
                val matches = individualAllergenPattern.findAll(allergensString)
                matches.forEach { match ->
                    allergens.add(match.groupValues[1])
                }
            }
        }

        val ingredientsPattern = """(.*)\(""".toRegex()
        if (allergens.isNotEmpty()) {
            val match = ingredientsPattern.findAll(line)
            match.first().groupValues[1].let { ingredientsString ->
                val individualIngredientPattern = """(\w+)""".toRegex()
                val matches = individualIngredientPattern.findAll(ingredientsString)
                matches.forEach { match ->
                    ingredients.add(match.groupValues[1])
                }
            }
            ingredientsAndAllergens.add(Pair(ingredients, allergens))
        }
    }
    return ingredientsAndAllergens
}