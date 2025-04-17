package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.GetCountriesFoodUseCase
import org.example.logic.GetSweetWithNoEggsUseCase
import logic.GymHelperUseCase
import org.example.logic.MealsRepository
import org.example.presentation.FoodConsoleUI
import org.example.logic.GetPotatoMealsUseCase

import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)

    if (isFileValid(csvFile, fileName)) return


    val mealReader = FoodCsvReader(csvFile)
    val mealParser = FoodCsvParser()
    val foodCsvRepository: MealsRepository = FoodCsvRepository(foodCsvParser = mealParser, foodCsvReader = mealReader)

    val sweetWithNoEggsUseCase: GetSweetWithNoEggsUseCase = GetSweetWithNoEggsUseCase(foodCsvRepository)
    val getCountriesFoodUseCase: GetCountriesFoodUseCase = GetCountriesFoodUseCase(foodCsvRepository)
    val getPotatoMealsUseCase = GetPotatoMealsUseCase(foodCsvRepository)
    val gymHelperUseCase: GymHelperUseCase = GymHelperUseCase(foodCsvRepository)

    val foodConsoleUI = FoodConsoleUI(
        sweetNoEggsUseCase = sweetWithNoEggsUseCase,
        getCountriesFoodUseCase = getCountriesFoodUseCase,
        getPotatoMealsUseCase = getPotatoMealsUseCase,
        gymHelperUseCase = gymHelperUseCase
    )
    foodConsoleUI.start()

}

private fun isFileValid(csvFile: File, fileName: String): Boolean {
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return true
    }
    return false
}