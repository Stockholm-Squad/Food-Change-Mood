package org.example.logic.usecases

import model.Meal
import org.example.logic.model.FoodChangeModeResults
import org.example.logic.repository.MealsRepository

class GetIngredientGameUseCase(
    private val repository: MealsRepository
) {
    data class Question(
        val mealName: String,
        val options: List<String>,
        val correctIngredient: String
    )

    private lateinit var currentQuestion: Question

    fun startIngredientGame(): Question {
        val meal = getRandomValidMeal()
        val correctIngredient = meal.ingredients!!.random()
        currentQuestion = Question(
            mealName = meal.name.toString(),
            options = generateOptions(correctIngredient),
            correctIngredient = correctIngredient
        )
        return currentQuestion
    }

    fun submitAnswer(selectedIngredient: String): Boolean {
        return selectedIngredient == currentQuestion.correctIngredient
    }


    private fun getRandomValidMeal(): Meal =
        when (val result = repository.getAllMeals()){
            is FoodChangeModeResults.Success -> {
                result.model.filter { !it.ingredients.isNullOrEmpty() && !it.name.isNullOrEmpty() }
                .takeIf { it.isNotEmpty() }
                ?.random()
                ?: throw IllegalStateException("No valid meals available") // to review
            }
            is FoodChangeModeResults.Fail -> {
                throw result.exception
            }
        }



    private fun generateOptions(correctIngredient: String): List<String> =
        when(val result = repository.getAllMeals()){
            is FoodChangeModeResults.Success -> {
                (result.model.asSequence()
                    .flatMap { it.ingredients.orEmpty().asSequence() }
                    .filter { it != correctIngredient }
                    .distinct()
                    .shuffled()
                    .take(2) + correctIngredient)
                    .shuffled().toList()
            }
            is FoodChangeModeResults.Fail -> {
                throw result.exception
            }
        }

}
