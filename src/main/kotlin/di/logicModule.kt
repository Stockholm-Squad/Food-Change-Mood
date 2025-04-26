package org.example.di

import org.example.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {

    single { GetHealthyFastFoodUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetDessertsWithNoEggsUseCase(get()) }
    single { GetCountryMealsUseCase(get()) }
    single { GetPotatoMealsUseCase(get()) }
    single { GetMealsForGymHelperUseCase(get()) }
    single { GetIngredientGameUseCase(get()) }
    single { GetMealsByDateUseCase(get(), get()) }
    single { GetItalianMealsForLargeGroupUseCase(get()) }
    single { GetMealForSoThinPeopleUseCase(get()) }

    single<SearchingByKmpUseCase> { SearchingByKmpUseCase() }
    single { GetMealByNameUseCase(get(), get()) }
    single { GetIraqiMealsUseCase(get()) }

    single { GetSeaFoodByProteinRankUseCase(get()) }
    single { GetMealForKetoDietUseCase(get()) }
    single { GetGuessPreparationTimeUseCase() }
    single { GetRandomMealUseCase(get()) }

}