package org.example.data.dataSource

import data.MealCsvParser
import data.MealCsvReader
import model.Meal

class MealCsvDataSource(
    private val mealCsvReader: MealCsvReader,
    private val mealCsvParser: MealCsvParser
) : MealDataSource {
    override fun getAllMeals(): List<Meal> {
        return try {
            mealCsvReader.readLinesFromFile()
                .mapNotNull { line ->
                    parseLine(line)
                }
        } catch (e: Exception) {
            //TODO throw exception here and delete returning of empty list
            println("Critical error reading CSV: ${e.message}")
            emptyList()
        }
    }

    private fun parseLine(line: String) = try {
        mealCsvParser.parseLine(line)
    } catch (e: Exception) {
        null
    }
}