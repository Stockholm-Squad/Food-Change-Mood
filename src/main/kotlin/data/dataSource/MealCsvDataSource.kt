package org.example.data.dataSource

import data.MealCsvParser
import data.MealCsvReader
import model.Meal
import org.example.model.Result

class MealCsvDataSource(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealDataSource {
    override fun getAllMeals(): Result<List<Meal>> {
        return when (val readResult = mealCsvReader.readLinesFromFile()) {
            is Result.Success -> {
                val meals = readResult.value.mapNotNull { line ->
                    parseLine(line)?.value
                }
                Result.Success(meals)
            }
            is Result.Failure -> {
                Result.Failure("Failed to read meals from CSV: ${readResult.errorMessage}", readResult.cause)
            }
        }
    }

    private fun parseLine(line: String) = try {
        when (val result = mealCsvParser.parseLine(line)) {
            is Result.Success -> result
            is Result.Failure -> {
                println("Failed to parse line: ${result.errorMessage}")
                null
            }
        }
    } catch (e: Exception) {
        println("Unexpected error parsing line: ${e.message}")
        null
    }
}
