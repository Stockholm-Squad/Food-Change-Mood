package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import logic.GymHelperUseCase
import org.example.data.FoodCsvRepository
import org.example.logic.MealsRepository
import org.example.presentation.FoodConsoleUI
import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile: File = File(fileName)
    if (isFileValid(csvFile, fileName)) return

    val csvReader: FoodCsvReader = FoodCsvReader(csvFile)
    val csvParser: FoodCsvParser = FoodCsvParser()

    val mealRepository: MealsRepository = FoodCsvRepository(csvReader, csvParser)
    val gymHelper: GymHelperUseCase = GymHelperUseCase(mealRepository)

    val foodConsoleUI = FoodConsoleUI(gymHelper)

    foodConsoleUI.start()

}

private fun isFileValid(csvFile: File, fileName: String): Boolean {
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return true
    }
    return false
}