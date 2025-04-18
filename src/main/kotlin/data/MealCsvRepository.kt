package org.example.data

import org.example.model.MealColumnIndex
import data.MealCsvParser
import data.MealCsvReader
import model.Meal
import org.example.logic.MealsRepository


class MealCsvRepository(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealsRepository {

    override fun getAllMeals(): List<Meal> {
        if (allMeals.isEmpty()) {
            allMeals = try {
                mealCsvReader.readLinesFromFile()
                    .mapNotNull { line ->
                        try {
                            mealCsvParser.parseLine(line)
                        } catch (e: Exception) {
                            println("Skipping invalid meal: ${line.getOrNull(MealColumnIndex.NAME.index)}")
                            null
                        }
                    }
            } catch (e: Exception) {
                println("Critical error reading CSV: ${e.message}")
                emptyList()
            }
        }
        return allMeals
    }

    companion object{
       private var allMeals: List<Meal> = emptyList()
    }
}