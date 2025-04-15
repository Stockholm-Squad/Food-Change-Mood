package org.example.data

import data.ColumnIndex
import data.FoodCsvParser
import data.FoodCsvReader
import model.Meal
import org.example.logic.MealsRepository


class FoodCsvRepository(
    private val foodCsvReader: FoodCsvReader,
    private val foodCsvParser: FoodCsvParser
) : MealsRepository {
    override fun getAllMeals(): List<Meal> {
        return try {
            foodCsvReader.readLinesFromFile()
                .mapNotNull { line ->
                    try {
                        foodCsvParser.parseOneLine(line)
                    } catch (e: Exception) {
                        println("Skipping invalid meal: ${line.getOrNull(ColumnIndex.NAME)}")
                        null
                    }
                }
        } catch (e: Exception) {
            println("Critical error reading CSV: ${e.message}")
            emptyList()
        }
    }
}