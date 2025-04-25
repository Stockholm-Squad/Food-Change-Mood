package org.example.data.dataSource

import model.Meal
import org.example.data.parser.MealParser
import org.example.data.reader.MealReader

class MealCsvDataSource(
    private val mealReader: MealReader,
    private val mealParser: MealParser
) : MealDataSource {
    override fun getAllMeals(): Result<List<Meal>> {
        return mealReader.readLinesFromFile().fold(
            onSuccess = { readResult ->
                val meals = readResult.mapNotNull { line ->
                    parseLine(line)
                }
                Result.success(meals)
            },
            onFailure = { exception -> Result.failure(exception) }
        )
    }

    private fun parseLine(line: String): Meal? {
        return mealParser.parseLine(line).fold(
            onSuccess = { result -> result },
            onFailure = { exception ->
                exception.printStackTrace()
                null
            }
        )
    }
}
