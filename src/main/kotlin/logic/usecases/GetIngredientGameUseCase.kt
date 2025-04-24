package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.model.IngredientQuestionModel

class GetIngredientGameUseCase(
    private val repository: MealsRepository
) {
    private var currentQuestion: IngredientQuestionModel? = null

    fun startIngredientGame(): Result<IngredientQuestionModel> {
        return try {
            val question = generateGameQuestion()
            currentQuestion = question
            Result.success(question)
        } catch (ex: Exception) {
            currentQuestion?.let { Result.success(it) }
                ?: Result.failure(ex)
        }
    }

    fun submitAnswer(selectedIngredient: String): Boolean {
        return selectedIngredient == currentQuestion?.correctIngredient
    }

    private fun generateGameQuestion(): IngredientQuestionModel {
        val meal = getRandomValidMeal().getOrThrow()
        val correctIngredient = meal.ingredients!!.random()
        val options = generateOptions(correctIngredient).getOrThrow()

        return IngredientQuestionModel(
            mealName = meal.name.orEmpty(),
            options = options,
            correctIngredient = correctIngredient
        )
    }

    private fun getRandomValidMeal(): Result<Meal> {
        return repository.getAllMeals().mapCatching { meals ->
            meals.filter { !it.ingredients.isNullOrEmpty() && !it.name.isNullOrEmpty() }
                .takeIf { it.isNotEmpty() }
                ?.random()
                ?: throw IllegalStateException("No valid meals available")
        }
    }

    private fun generateOptions(correctIngredient: String): Result<List<String>> {
        return repository.getAllMeals().mapCatching { meals ->
            getGeneratedOptions(meals, correctIngredient)
        }
    }

    private fun getGeneratedOptions(
        meals: List<Meal>,
        correctIngredient: String
    ): List<String> {
        return (meals.asSequence()
            .flatMap { it.ingredients.orEmpty().asSequence() }
            .filter { it != correctIngredient }
            .distinct()
            .shuffled()
            .take(2) + correctIngredient)
            .shuffled().toList()
    }
}
