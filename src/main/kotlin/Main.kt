package org.example

import IngredientGameUseCase
import data.FoodCsvParser
import data.FoodCsvReader
import logic.GetHealthFastFoodUseCase
import org.example.data.FoodCsvRepository
import logic.GymHelperUseCase
import logic.SearchMealByNameUseCase
import org.example.logic.*
import org.example.presentation.FoodConsoleUI

import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)

    if (isFileValid(csvFile, fileName)) return


    val mealReader = FoodCsvReader(csvFile)
    val mealParser = FoodCsvParser()
    val foodCsvRepository: MealsRepository = FoodCsvRepository(foodCsvParser = mealParser, foodCsvReader = mealReader)
    val getHealthFastFoodUseCase= GetHealthFastFoodUseCase(foodCsvRepository)
    val sweetWithNoEggsUseCase = GetSweetWithNoEggsUseCase(foodCsvRepository)
    val getCountriesFoodUseCase = GetCountriesFoodUseCase(foodCsvRepository)
    val getPotatoMealsUseCase = GetPotatoMealsUseCase(foodCsvRepository)
    val gymHelperUseCase = GymHelperUseCase(foodCsvRepository)
    val ingredientGameUseCase = IngredientGameUseCase(foodCsvRepository)
    val getSeaFoodByProteinRankUseCase = GetSeaFoodByProteinRankUseCase(foodCsvRepository)
    val searchByAddDateUseCase = SearchByAddDateUseCase(foodCsvRepository)
    val italianMealsForLargeGroupUseCase = ItalianMealsForLargeGroupUseCase(foodCsvRepository)
    val getEasyFoodSuggestionsUseCase = GetEasyFoodSuggestionsUseCase(foodCsvRepository)
    val soThinProblem = SoThinProblem(foodCsvRepository)

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
        soThinProblem = soThinProblem,
        searchMealByNameUseCase = SearchMealByNameUseCase(foodCsvRepository)
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