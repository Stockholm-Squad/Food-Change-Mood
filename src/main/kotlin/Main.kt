package org.example

import presentation.FoodConsoleUI
import java.io.File


fun main() {
    val foodConsoleUI: FoodConsoleUI = FoodConsoleUI()
    val fileName = "food.csv"
    val csvFile = File(fileName)

    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    foodConsoleUI.start()

}