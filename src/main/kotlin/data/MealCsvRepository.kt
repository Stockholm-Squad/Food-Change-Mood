package org.example.data

import data.MealCsvParser
import data.MealCsvReader
import model.Meal
import org.example.logic.MealsRepository


class MealCsvRepository(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealsRepository {

    override fun getAllMeals(): List<Meal> {
        if (allMeals.isNotEmpty()) return allMeals

        allMeals = try {
            mealCsvReader.readLinesFromFile()
                .mapNotNull { line ->
                    parseLine(line)
                }
        } catch (e: Exception) {
            println("Critical error reading CSV: ${e.message}")
            emptyList()
        }

        return allMeals
    }

    private fun parseLine(line: String) = try {
        mealCsvParser.parseLine(line)
    } catch (e: Exception) {
        null
    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }
}