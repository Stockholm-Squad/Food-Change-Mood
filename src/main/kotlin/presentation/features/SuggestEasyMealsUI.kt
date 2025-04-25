package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetEasyFoodSuggestionsUseCase

class SuggestEasyMealsUI(
    private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase
) {

    fun showEasySuggestions() {
        println("⏱️ Easy meals coming up!")
        getEasyFoodSuggestionsUseCase.getEasyFood().fold(
            onSuccess = ::showEasyFoodMeals,
            onFailure = ::showFailureMessage
        )
    }

    private fun showEasyFoodMeals(easyFoodMeals: List<Meal>) {
        easyFoodMeals.takeIf { it.isNotEmpty() }?.forEach {
            println(it)
        }
    }

    private fun showFailureMessage(throwable: Throwable) {
        println(throwable.message)
    }
}

