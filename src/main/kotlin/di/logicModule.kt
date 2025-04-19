package org.example.di

import org.example.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {

    single { GetHealthyFastFoodUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetDessertsWithNoEggs(get()) }
    single { GetCountryFoodUseCase(get()) }
    single { GetPotatoMealsUseCase(get()) }
    single { GymHelperUseCase(get()) }
    single { IngredientGameUseCase(get()) }
    single { SearchByAddDateUseCase(get()) }
    single { ItalianMealsForLargeGroupUseCase(get()) }
    single { SoThinProblemUseCase(get()) }

    single { SearchMealByNameUseCase(get()) }
    single { GetIraqiMealsUseCase(get()) }

    single { GetSeaFoodByProteinRankUseCase(get()) }
    single { KetoDietUseCase(get()) }

    single { GetGuessGameUseCase(get()) }

}