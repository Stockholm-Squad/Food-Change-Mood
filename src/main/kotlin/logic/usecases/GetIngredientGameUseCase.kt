package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.model.IngredientQuestionModel

class GetIngredientGameUseCase(
    private val repository: MealsRepository
) {
    private lateinit var currentQuestion: IngredientQuestionModel

    fun startIngredientGame(): IngredientQuestionModel {
        val meal = getRandomValidMeal()
        val correctIngredient = meal.ingredients!!.random()
        currentQuestion = IngredientQuestionModel(
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
        repository.getAllMeals().fold(
            onSuccess = {meals ->
                meals.filter { !it.ingredients.isNullOrEmpty() && !it.name.isNullOrEmpty() }
                .takeIf { it.isNotEmpty() }
                ?.random()
                ?: throw IllegalStateException("No valid meals available") // to review
            },
            onFailure = {
                throw it
            }
        )



    private fun generateOptions(correctIngredient: String): List<String> =
        repository.getAllMeals().fold(
            onSuccess = {meals ->
                (meals.asSequence()
                    .flatMap { it.ingredients.orEmpty().asSequence() }
                    .filter { it != correctIngredient }
                    .distinct()
                    .shuffled()
                    .take(2) + correctIngredient)
                    .shuffled().toList()
            },
            onFailure = {
                throw it
            }
        )

}
