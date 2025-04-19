package org.example.di

import IngredientGameUseCase
import logic.GetHealthFastFoodUseCase
import logic.GetIraqiMealsUseCase
import logic.GymHelperUseCase
import logic.SearchMealByNameUseCase
import org.example.logic.*
import org.koin.dsl.module

val logicModule = module {

    single { GetHealthFastFoodUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetDessertsWithNoEggs(get()) }
    single { GetCountryFoodUseCase(get()) }
    single { GetPotatoMealsUseCase(get()) }
    single { GymHelperUseCase(get()) }
    single { IngredientGameUseCase(get()) }
    single { SearchByAddDateUseCase(get()) }
    single { ItalianMealsForLargeGroupUseCase(get()) }
    single { SoThinProblem(get()) }

    single { SearchMealByNameUseCase(get()) }
    single { GetIraqiMealsUseCase(get()) }

    single { GetSeaFoodByProteinRankUseCase(get()) }
    single { KetoDietUseCase(get()) }

    single { GetGuessGameUseCase(get()) }

}