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
            getMealsForGymHelperUseCase = get(),
            getIngredientGameUseCase = get(),
            getSeaFoodByProteinRankUseCase = get(),
            getMealsByDateUseCase = get(),
            getMealsForLargeGroupUseCase = get(),
            getMealByNameUseCase = get(),
            soThinProblem = get(),
            getIraqiMealsUseCase = get(),
            getMealForKetoDietUseCase = get(),
            getGuessGameUseCase = get ()
        )
    }
}