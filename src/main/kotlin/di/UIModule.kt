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
    factory { GuessGameUI(get(),get()) }
    factory { GymHelperUI(get(), get(), get())}
    factory { IngredientGameUI(get(), get(), get()) }
    factory { ItalianLargeGroupMealsUI(get()) }
    factory { KetoDietMealUI(get() , get() , get()) }
    factory { PotatoLoversUI(get()) }
    factory { ProteinSeafoodRankingUI(get()) }
    factory { SearchMealByNameUI(get()) }
    factory { SearchByAddDateUI(get(), get()) }
    factory { SuggestEasyMealsUI(get()) }
    factory { SuggestSweetNoEggsUI(get()) }
    factory { SuggestMealForSoThinPeopleUI(get()) }
}