package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.KetoDietUseCase
import org.example.presentation.FoodConsoleUI
import java.io.File


fun main() {
    val fileName = "food.csv"
    val csvFile = File(fileName)
    val ketoDietUseCase = KetoDietUseCase(FoodCsvRepository(FoodCsvReader(csvFile), FoodCsvParser()))
    val foodConsoleUI = FoodConsoleUI(ketoDietUseCase)


    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    foodConsoleUI.start()

}