package org.example.di

import org.example.presentation.FoodConsoleUI
import org.koin.dsl.module

val uiModule = module {
    single<FoodConsoleUI> {
        FoodConsoleUI(
            getHealthFastFoodUseCase = get(),
            getEasyFoodSuggestionsUseCase = get(),
            sweetNoEggsUseCase = get(),
            getCountryFoodUseCase = get(),
            getPotatoMealsUseCase = get(),
            gymHelperUseCase = get(),
            ingredientGameUseCase = get(),
            getSeaFoodByProteinRankUseCase = get(),
            searchByAddDateUseCase = get(),
            italianMealsForLargeGroupUseCase = get(),
            getMealByNameUseCase = get(),
            soThinProblem = get(),
            getIraqiMealsUseCase = get(),
            ketoDietUseCase = get(),
            getGuessGameUseCase = get ()
        )
    }
}