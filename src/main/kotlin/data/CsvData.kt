package org.example.data

import model.Meal
import org.example.logic.MealRepository
import java.io.File

class CsvData(private val csvFile: File) : MealRepository {
    override fun getAllMeals(): List<Meal> {
        val allMeals: MutableList<Meal> = mutableListOf()
        val meals = csvFile.readLines()
        meals.drop(3).forEach {
            val meal = splitMealFromCsv(it.replace("'", ""))
            println(meal)
            println()
        }

        return allMeals
    }

    private fun splitMealFromCsv(input: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var insideQuotes = false
        var bracketLevel = 0
        for (element in input) {
            val c = element

            when (c) {
                '"' -> {
                    insideQuotes = !insideQuotes
                    current.append(c)
                }

                '[' -> {
                    bracketLevel++
                    current.append(c)
                }

                ']' -> {
                    bracketLevel--
                    current.append(c)
                }
                ',' -> {
                    if (bracketLevel == 0) {
                        result.add(current.toString().trim())
                        current.clear()
                    } else {
                        current.append(c)
                    }
                }

                else -> current.append(c)
            }


        }


        return result
    }

}


/*

     val nutrition = result[ColumnIndex.NUTRITION].replace("[", "").replace("]", "").split(",")


 Meal(
            id = result[ColumnIndex.ID].toInt(),
            name = result[ColumnIndex.NAME],
            tags = result[ColumnIndex.TAGS].split(","),
            numberOfIngredients = result[ColumnIndex.N_INGREDIENTS].toInt(),
            preparationTime = result[ColumnIndex.MINUTES].toInt(),
            steps = result[ColumnIndex.STEPS].split(","),
            addedDate = result[ColumnIndex.SUBMITTED],
            nutrition = Nutrition(
                saturatedFat = nutrition[ColumnIndex.SATURATED_FAT_INDEX].toFloatOrNull(),
                carbohydrates = nutrition[ColumnIndex.CARBOHYDRATES].toFloatOrNull(),
                sugar = nutrition[ColumnIndex.SUGAR_INDEX].toFloatOrNull(),
                sodium = nutrition[ColumnIndex.SODIUM].toFloatOrNull(),
                calories = nutrition[ColumnIndex.CALORIES_INDEX].toFloatOrNull(),
                totalFat = nutrition[ColumnIndex.TOTAL_FAT_INDEX].toFloatOrNull(),
                protein = nutrition[ColumnIndex.PROTEIN_INDEX].toFloatOrNull()
            ),
            description = result[ColumnIndex.DESCRIPTION],
            numberOfSteps = result[ColumnIndex.N_STEPS].toInt(),
            ingredients = result[ColumnIndex.INGREDIENTS].split(","),
            contributorId = result[ColumnIndex.CONTRIBUTOR_ID].toInt()
        )
 */