package org.example

import IngredientGameUseCase
import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.presentation.FoodConsoleUI
import org.example.presentation.IngredientGameUI
import java.io.File


fun main() {
    val fileName = "food.csv"
    val csvFile = File(fileName)


    val repository = FoodCsvRepository(FoodCsvReader(csvFile),FoodCsvParser())
    val ingredientGame  = IngredientGameUseCase(repository)
    
    val foodConsoleUI = FoodConsoleUI(ingredientGame)

    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    foodConsoleUI.start()

}