package org.example

import CsvLineHandler
import IngredientGameUseCase
import data.MealCsvParser
import data.MealCsvReader
import logic.GetHealthFastFoodUseCase
import logic.GetIraqiMealsUseCase
import org.example.data.MealCsvRepository
import logic.GymHelperUseCase
import logic.SearchMealByNameUseCase
import org.example.data.CsvLineFormatter
import org.example.logic.*
import data.FoodCsvParser
import data.FoodCsvReader
import org.example.logic.KetoDietUseCase
import org.example.presentation.FoodConsoleUI

import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)

    if (isFileValid(csvFile, fileName)) return


    val mealReader = MealCsvReader(csvFile, CsvLineHandler())
    val mealParser = MealCsvParser(CsvLineFormatter())
    val mealCsvRepository: MealsRepository = MealCsvRepository(mealCsvParser = mealParser, mealCsvReader = mealReader)
    val getHealthFastFoodUseCase= GetHealthFastFoodUseCase(mealCsvRepository)
    val sweetWithNoEggsUseCase: GetSweetWithNoEggsUseCase = GetSweetWithNoEggsUseCase(mealCsvRepository)
    val getCountriesFoodUseCase: GetCountriesFoodUseCase = GetCountriesFoodUseCase(mealCsvRepository)
    val getPotatoMealsUseCase = GetPotatoMealsUseCase(mealCsvRepository)
    val gymHelperUseCase: GymHelperUseCase = GymHelperUseCase(mealCsvRepository)
    val ingredientGameUseCase = IngredientGameUseCase(mealCsvRepository)
    val getSeaFoodByProteinRankUseCase = GetSeaFoodByProteinRankUseCase(mealCsvRepository)
    val searchByAddDateUseCase = SearchByAddDateUseCase(mealCsvRepository)
    val italianMealsForLargeGroupUseCase = ItalianMealsForLargeGroupUseCase(mealCsvRepository)
    val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase = GetEasyFoodSuggestionsUseCase(mealCsvRepository)
    val soThinProblem: SoThinProblem = SoThinProblem(mealCsvRepository)
    val ketoDietUseCase = KetoDietUseCase(mealCsvRepository)
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
        searchMealByNameUseCase = SearchMealByNameUseCase(mealCsvRepository),
        getIraqiMealsUseCase = GetIraqiMealsUseCase(mealCsvRepository)
        ketoDietUseCase = ketoDietUseCase,
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