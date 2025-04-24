package org.example.di


import org.example.presentation.FoodConsoleUI
import org.example.presentation.features.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    singleOf(::FoodConsoleUI)

    factory { ExploreCountryMealsUI(get(), get(), get()) }
    factory { GetHealthyFastFoodMealsUI(get(), get()) }
    factory { GetIraqiMealsUI(get(), get()) }
    factory { GuessGameUI(get(), get(), get(), get()) }
    factory { GymHelperUI(get(), get(), get()) }
    factory { IngredientGameUI(get(), get(), get()) }
    factory { ItalianLargeGroupMealsUI(get(), get(), get()) }
    factory { KetoDietMealUI(get(), get(), get()) }
    factory { PotatoLoversUI(get()) }
    factory { ProteinSeafoodRankingUI(get(), get()) }
    factory { SearchMealByNameUI(get(), get(), get()) }
    factory { SearchByAddDateUI(get(), get(), get(), get()) }
    factory { SuggestEasyMealsUI(get()) }
    factory { SuggestSweetNoEggsUI(get(), get(), get()) }
    factory { SuggestMealForSoThinPeopleUI(get(), get(), get()) }
}