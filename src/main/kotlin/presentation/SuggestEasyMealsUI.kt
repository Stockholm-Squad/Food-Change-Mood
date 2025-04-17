package presentation;

import org.example.logic.GetEasyFoodSuggestionsUseCase

class SuggestEasyMealsUI (private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase){

     fun showEasySuggestions() {
        println("⏱️ Quick & tasty meals coming up!")
         getEasyFoodSuggestionsUseCase.getEasyFood().forEach {
             println(it)
         }
    }
}
