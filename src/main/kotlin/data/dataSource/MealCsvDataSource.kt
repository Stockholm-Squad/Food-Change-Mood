package org.example.data.dataSource

import data.MealCsvParser
import data.MealCsvReader
import data.ParsingResult
import model.Meal

class MealCsvDataSource(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealDataSource {
    override fun getAllMeals(): List<Meal> {
        return try {
            mealCsvReader.readLinesFromFile()
                .mapNotNull { line ->
                    parseLine(line)?.value
                }
        } catch (e: Exception) {
            throw DataSourceException("Critical error reading CSV", e)
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


// Custom exception for data source errors
class DataSourceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)