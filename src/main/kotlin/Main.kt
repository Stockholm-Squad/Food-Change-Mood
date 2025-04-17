package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.GetSeaFoodByProteinRankUseCase
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
    val seaFoodByProteinRank: GetSeaFoodByProteinRankUseCase = GetSeaFoodByProteinRankUseCase(mealRepository)
    val foodConsoleUI = FoodConsoleUI(seaFoodByProteinRank)
    foodConsoleUI.start()
    // Check if the file exists before proceeding

}

private fun isFileValid(csvFile: File, fileName: String): Boolean {
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return true
    }
    return false
}