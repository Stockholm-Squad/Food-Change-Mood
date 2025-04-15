package org.example.data

import data.CsvFileFoodParser
import data.CsvFoodReader
import model.Meal
import org.example.logic.IMealsRepository

class CsvMealsRepository(
    private val csvFileFoodParser: CsvFileFoodParser,
    private val csvFoodReader: CsvFoodReader
) : IMealsRepository {
    override fun getAllMeals(): List<Meal> {
        val allMeals: MutableList<Meal> = mutableListOf()
        csvFoodReader.readLinesFromFile().forEach { lieOfcsv ->
            val newMeal = csvFileFoodParser.parseOnLine(lieOfcsv)
            allMeals.add(newMeal)
        }
        return allMeals
    }

}
