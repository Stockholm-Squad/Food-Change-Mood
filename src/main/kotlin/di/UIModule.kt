package org.example.di

import org.example.presentation.FoodConsoleUI
import org.koin.core.scope.get
import org.koin.dsl.module

val uiModule = module {
    single<FoodConsoleUI> {
        FoodConsoleUI(
            getHealthFastFoodUseCase = get(),
            getEasyFoodSuggestionsUseCase = get(),
            sweetNoEggsUseCase = get(),
            getCountriesFoodUseCase = get(),
            getPotatoMealsUseCase = get(),
            gymHelperUseCase = get(),
            ingredientGameUseCase = get(),
            getSeaFoodByProteinRankUseCase = get(),
            searchByAddDateUseCase = get(),
            italianMealsForLargeGroupUseCase = get(),
            searchMealByNameUseCase = get(),
            soThinProblem = get(),
            getIraqiMealsUseCase = get(),
            ketoDietUseCase = get(),
            getGuessGameUseCase = get (),


        )
    }
}