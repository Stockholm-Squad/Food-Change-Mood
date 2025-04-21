package org.example.data.dataSource

import data.MealCsvParser
import data.MealCsvReader
import model.Meal
import org.example.logic.model.Results

class MealCsvDataSource(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealDataSource {
    override fun getAllMeals(): Results<List<Meal>> {
        return when (val readResult = mealCsvReader.readLinesFromFile()) {
            is Results.Success -> {
                val meals = readResult.model.mapNotNull { line ->
                    parseLine(line)?.model
                }
                Results.Success(meals)
            }
            is Results.Fail -> {
               Results.Fail(readResult.exception)
            }
        }
    }

    private fun parseLine(line: String) = try {
        when (val result = mealCsvParser.parseLine(line)) {
            is Results.Success -> result
            is Results.Fail -> {
                result.exception.printStackTrace()
                null
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
