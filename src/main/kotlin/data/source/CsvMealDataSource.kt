package org.example.data.source

import data.MealsFileParser
import data.MealsFileReader
import model.Meal

class CsvMealDataSource(
    private val mealsFileParser: MealsFileParser,
    private val mealsFileReader: MealsFileReader
) {
    fun loadMeals():List<Meal>{
        return mealsFileReader.readLinesFromFile().map {
            mealsFileParser.parseLine(it)
        }
    }
}