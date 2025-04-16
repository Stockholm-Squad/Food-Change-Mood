package org.example.data.source


import model.Meal
import org.example.data.MealFileParser
import org.example.data.MealFileReader

class CsvMealDataSource(
    private val mealsFileParser: MealFileParser,
    private val mealsFileReader: MealFileReader
) {
    fun loadMeals():List<Meal>{
        return mealsFileReader.readLinesFromFile().map {
            mealsFileParser.parseLine(it)
        }
    }
}