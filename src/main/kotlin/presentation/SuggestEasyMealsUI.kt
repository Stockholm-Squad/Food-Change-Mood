package presentation;

import org.example.logic.usecases.GetEasyFoodSuggestionsUseCase

class SuggestEasyMealsUI (private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase) {

    fun showEasySuggestions() {
        println("⏱️ Easy meals coming up!")
        getEasyFoodSuggestionsUseCase.getEasyFood().takeIf { it.isNotEmpty() }?.forEach {
            println(it)
        } ?: throw NoSuchElementException("No easy food suggestions available")
    }
}