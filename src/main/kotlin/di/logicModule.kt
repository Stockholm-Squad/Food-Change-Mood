package org.example.di

import org.example.logic.usecases.SearchingByKmpUseCase
import org.example.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {

    single { GetHealthyFastFoodUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetDessertsWithNoEggsUseCase(get()) }
    single { GetCountryFoodUseCase(get()) }
    single { GetPotatoMealsUseCase(get()) }
    single { GetMealsForGymHelperUseCase(get()) }
    single { GetIngredientGameUseCase(get()) }
    single { GetMealsByDateUseCase(get()) }
    single { GetMealsForLargeGroupUseCase(get()) }
    single { GetMealsForSoThinProblemUseCase(get()) }

    single<SearchingByKmpUseCase> { SearchingByKmpUseCase() }
    single { GetMealByNameUseCase(get(), get())}
    single { GetIraqiMealsUseCase(get()) }

    single { GetSeaFoodByProteinRankUseCase(get()) }
    single { GetMealForKetoDietUseCase(get()) }

    single { GetGuessGameUseCase(get()) }

}