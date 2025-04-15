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
        return csvFoodReader.readLinesFromFile().map {
            csvFileFoodParser.parseOnLine(it)
        }
    }

}
