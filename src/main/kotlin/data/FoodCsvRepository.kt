package org.example.data

import data.MealColumnIndex
import data.FoodCsvParser
import data.FoodCsvReader
import model.Meal
import org.example.logic.MealsRepository


class FoodCsvRepository(
    private val foodCsvReader: FoodCsvReader,
    private val foodCsvParser: FoodCsvParser
) : MealsRepository {

    override fun getAllMeals(): List<Meal> {
        if (allMeals.isEmpty()) {
            allMeals = try {
                foodCsvReader.readLinesFromFile()
                    .mapNotNull { line ->
                        try {
                            foodCsvParser.parseLine(line)
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