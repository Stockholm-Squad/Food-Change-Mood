package org.example

import org.example.data.CsvData
import org.example.logic.MealRepository
import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)
    val mealRepository : MealRepository  = CsvData(csvFile)
    mealRepository.getAllMeals().also {
        println(it)
        println()
    }

}