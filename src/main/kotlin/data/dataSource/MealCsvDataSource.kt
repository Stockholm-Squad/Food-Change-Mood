package org.example.data.dataSource

import data.MealCsvParser
import data.MealCsvReader
import model.Meal
import org.example.Results.ParsingResult
import org.example.Results.ReaderResult

class MealCsvDataSource(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealDataSource {
    override fun getAllMeals(): ReaderResult<List<Meal>> {
        return when (val readResult = mealCsvReader.readLinesFromFile()) {
            is ReaderResult.Success -> {
                val meals = readResult.value.mapNotNull { line ->
                    parseLine(line)?.value
                }
                ReaderResult.Success(meals)
            }
            is ReaderResult.Failure -> {
                ReaderResult.Failure("Failed to read meals from CSV: ${readResult.errorMessage}", readResult.cause)
            }
        }
    }

    private fun parseLine(line: String) = try {
        when (val result = mealCsvParser.parseLine(line)) {
            is ParsingResult.Success -> result
            is ParsingResult.Failure -> {
                println("Failed to parse line: ${result.errorMessage}")
                null
            }
        }
    } catch (e: Exception) {
        println("Unexpected error parsing line: ${e.message}")
        null
    }
}
