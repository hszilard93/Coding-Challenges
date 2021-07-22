package day21

/*
Challenge at https://adventofcode.com/2020/day/21
 */

import java.io.File

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

    val ingredientsExcludedSet = HashSet<String>()
    // we now have to enumerate the ingredients which can't possibly contain any allergens at this stage
    ingredientsSet.forEach { ingredient ->
        var isSuspicious = false
        run loop@{
            allergensIngredientsMap.values.forEach { suspiciousIngredients ->
                if (ingredient in suspiciousIngredients) {
                    isSuspicious = true
                    return@loop
                }
            }
        }
        if (!isSuspicious) {
            ingredientsExcludedSet.add(ingredient)
        }
    }
    // And now we have to count how often these 'not suspicious' ingredients occur in the original list
    var excludedOccurrence = 0
    ingredientsAllergensList.forEach { pair ->
        pair.first.forEach { ingredient ->
            if (ingredient in ingredientsExcludedSet) {
                excludedOccurrence += 1
            }
        }
    }

    println(allergensIngredientsMap)
    println("No allergens: $ingredientsExcludedSet")
    println("No allergens occurrence: $excludedOccurrence")
}

fun getIngredientsAndAllergensFromInput(fileName: String): List<Pair<List<String>, List<String>>> {
    val ingredientsAndAllergens = ArrayList<Pair<List<String>, List<String>>>()

    val currentDir = System.getProperty("user.dir") + "/src/day21"
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