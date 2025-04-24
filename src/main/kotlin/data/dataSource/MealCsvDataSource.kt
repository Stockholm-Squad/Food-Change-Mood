package org.example.data.dataSource

import org.example.data.parser.MealCsvParser
import org.example.data.reader.MealCsvReader
import model.Meal

class MealCsvDataSource(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealDataSource {
    override fun getAllMeals(): Result<List<Meal>> {
        return mealCsvReader.readLinesFromFile().fold(
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
        return mealCsvParser.parseLine(line).fold(
            onSuccess = { result -> result },
            onFailure = { exception ->
                exception.printStackTrace()
                null
            }
        )
    }
}
