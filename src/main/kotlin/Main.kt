package org.example

import appModule
import dataModule
import org.example.di.uiModule
import org.example.di.logicModule
import org.example.presentation.FoodConsoleUI
import org.example.utils.Constants
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File


fun main() {

    val csvFile = File(Constants.MEAL_CSV_FILE)

    if (isFileValid(csvFile, Constants.MEAL_CSV_FILE)) return


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
   val getGuessGameUseCase=GetGuessGameUseCase(mealCsvRepository)
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
        getIraqiMealsUseCase = GetIraqiMealsUseCase(mealCsvRepository),
        getGuessGameUseCase=getGuessGameUseCase
    )

    startKoin {
        modules(appModule, dataModule, logicModule, uiModule)
    }

    val foodConsoleUI: FoodConsoleUI = getKoin().get()

    foodConsoleUI.start()

}

private fun isFileValid(csvFile: File, fileName: String): Boolean {
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return true
    }
    return false
}