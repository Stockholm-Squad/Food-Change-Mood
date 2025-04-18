package org.example

import CsvLineHandler
import IngredientGameUseCase
import data.MealCsvParser
import data.FoodCsvReader
import logic.GetHealthFastFoodUseCase
import org.example.data.FoodCsvRepository
import logic.GymHelperUseCase
import org.example.logic.*
import org.example.presentation.FoodConsoleUI

import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)

    if (isFileValid(csvFile, fileName)) return


    val mealReader = FoodCsvReader(csvFile, CsvLineHandler())
    val mealParser = MealCsvParser()
    val foodCsvRepository: MealsRepository = FoodCsvRepository(mealCsvParser = mealParser, foodCsvReader = mealReader)
    val getHealthFastFoodUseCase= GetHealthFastFoodUseCase(foodCsvRepository)
    val sweetWithNoEggsUseCase: GetSweetWithNoEggsUseCase = GetSweetWithNoEggsUseCase(foodCsvRepository)
    val getCountriesFoodUseCase: GetCountriesFoodUseCase = GetCountriesFoodUseCase(foodCsvRepository)
    val getPotatoMealsUseCase = GetPotatoMealsUseCase(foodCsvRepository)
    val gymHelperUseCase: GymHelperUseCase = GymHelperUseCase(foodCsvRepository)
    val ingredientGameUseCase = IngredientGameUseCase(foodCsvRepository)
    val getSeaFoodByProteinRankUseCase = GetSeaFoodByProteinRankUseCase(foodCsvRepository)
    val searchByAddDateUseCase = SearchByAddDateUseCase(foodCsvRepository)
    val italianMealsForLargeGroupUseCase = ItalianMealsForLargeGroupUseCase(foodCsvRepository)
    val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase = GetEasyFoodSuggestionsUseCase(foodCsvRepository)
    val soThinProblem: SoThinProblem = SoThinProblem(foodCsvRepository)

    val foodConsoleUI = FoodConsoleUI(
        sweetNoEggsUseCase = sweetWithNoEggsUseCase,
        getCountriesFoodUseCase = getCountriesFoodUseCase,
        getPotatoMealsUseCase = getPotatoMealsUseCase,
        gymHelperUseCase = gymHelperUseCase,
        ingredientGameUseCase = ingredientGameUseCase,
        getSeaFoodByProteinRankUseCase = getSeaFoodByProteinRankUseCase,
        searchByAddDateUseCase = searchByAddDateUseCase,
        italianMealsForLargeGroupUseCase = italianMealsForLargeGroupUseCase,
        getEasyFoodSuggestionsUseCase = getEasyFoodSuggestionsUseCase,
        getHealthFastFoodUseCase = getHealthFastFoodUseCase,
        soThinProblem = soThinProblem
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