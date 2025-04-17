package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.GetEasyFoodSuggestionsUseCase
import org.example.logic.MealsRepository
import org.example.presentation.FoodConsoleUI
import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)
    val csvReader: FoodCsvReader = FoodCsvReader(csvFile)
    val csvParser: FoodCsvParser = FoodCsvParser()

    val mealRepository: MealsRepository = FoodCsvRepository(csvReader, csvParser)
    val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase = GetEasyFoodSuggestionsUseCase(mealRepository)
    val foodConsoleUI = FoodConsoleUI(getEasyFoodSuggestionsUseCase)
    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    foodConsoleUI.start()

}