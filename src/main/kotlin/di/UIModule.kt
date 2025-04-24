package org.example.di


import org.example.presentation.FoodConsoleUI
import org.example.presentation.features.ExploreCountryFoodUI
import org.example.presentation.features.GetHealthyFastFoodMealsUI
import org.example.presentation.features.GetIraqiMealsUI
import org.example.presentation.features.GuessGameUI
import org.example.presentation.features.GymHelperUI
import org.example.presentation.features.IngredientGameUI
import org.example.presentation.features.ItalianLargeGroupMealsUI
import org.example.presentation.features.KetoDietMealUI
import org.example.presentation.features.PotatoLoversUI
import org.example.presentation.features.ProteinSeafoodRankingUI
import org.example.presentation.features.SearchByAddDateUI
import org.example.presentation.features.SearchMealByNameUI
import org.example.presentation.features.SuggestEasyMealsUI
import org.example.presentation.features.SuggestMealForSoThinPeopleUI
import org.example.presentation.features.SuggestSweetNoEggsUI
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    singleOf(::FoodConsoleUI)

    factory { ExploreCountryFoodUI(get()) }
    factory { GetHealthyFastFoodMealsUI(get(),get()) }
    factory { GetIraqiMealsUI(get()) }
    factory { GuessGameUI(get(),get()) }
    factory { GymHelperUI(get(), get(), get())}
    factory { IngredientGameUI(get()) }
    factory { ItalianLargeGroupMealsUI(get()) }
    factory { KetoDietMealUI(get()) }
    factory { PotatoLoversUI(get()) }
    factory { ProteinSeafoodRankingUI(get()) }
    factory { SearchMealByNameUI(get()) }
    factory { SearchByAddDateUI(get(), get()) }
    factory { SuggestEasyMealsUI(get()) }
    factory { SuggestSweetNoEggsUI(get()) }
    factory { SuggestMealForSoThinPeopleUI(get()) }
}