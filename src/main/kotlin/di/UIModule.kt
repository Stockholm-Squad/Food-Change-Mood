package org.example.di


import org.example.presentation.FoodConsoleUI
import org.example.presentation.features.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    singleOf(::FoodConsoleUI)

    factory { ExploreCountryFoodUI(get()) }
    factory { GetHealthyFastFoodMealsUI(get()) }
    factory { GetIraqiMealsUI(get()) }
    factory { GuessGameUI(get(), get()) }
    factory { GymHelperUI(get(), get(), get()) }
    factory { IngredientGameUI(get()) }
    factory { ItalianLargeGroupMealsUI(get(), get(), get()) }
    factory { KetoDietMealUI(get()) }
    factory { PotatoLoversUI(get()) }
    factory { ProteinSeafoodRankingUI(get(), get()) }
    factory { SearchMealByNameUI(get(), get(), get()) }
    factory { SearchByAddDateUI(get(), get(), get(), get()) }
    factory { SuggestEasyMealsUI(get()) }
    factory { SuggestSweetNoEggsUI(get()) }
    factory { SuggestMealForSoThinPeopleUI(get(), get(), get()) }
}