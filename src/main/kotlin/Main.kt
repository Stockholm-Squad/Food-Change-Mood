package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.MealsRepository
import org.example.presentation.FoodConsoleUI
import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)

    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    val mealReader = FoodCsvReader(csvFile)
    val mealParser = FoodCsvParser()
    val foodCsvRepository: MealsRepository = FoodCsvRepository(foodCsvParser = mealParser, foodCsvReader = mealReader)
    val foodConsoleUI = FoodConsoleUI(foodCsvRepository)
    foodConsoleUI.start()

}