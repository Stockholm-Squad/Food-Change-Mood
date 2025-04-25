package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.model.IngredientQuestionModel

class GetIngredientGameUseCase(
    private val repository: MealsRepository
) {
    private lateinit var currentQuestion: IngredientQuestionModel

    fun startIngredientGame(): Result<IngredientQuestionModel> {
        generateGameQuestion().fold(
            onSuccess = {
                currentQuestion = it
                return Result.success(it)
            },
            onFailure = {
                return Result.failure(it)
            }
        )
    }

    fun submitAnswer(selectedIngredient: String): Boolean {
        return selectedIngredient == currentQuestion.correctIngredient
    }

    private fun generateGameQuestion(): Result<IngredientQuestionModel> {
        getRandomValidMeal().fold(
            onSuccess = { meal ->
                val correctIngredient = meal.ingredients!!.random()
                generateOptions(correctIngredient).fold(
                    onSuccess = { options ->
                        return Result.success(IngredientQuestionModel(meal.name.toString(), options, correctIngredient))
                    },
                    onFailure = {
                        return Result.failure(it)
                    }
                )
            },
            onFailure = {
                return Result.failure(it)
            }
        )
    }

    private fun getRandomValidMeal(): Result<Meal> =
        repository.getAllMeals().fold(
            onSuccess = { meals ->
                val validMeals = meals.filter { !it.ingredients.isNullOrEmpty() && !it.name.isNullOrEmpty() }
                return if (validMeals.isEmpty()) {
                    Result.failure(IllegalStateException("no meals"))
                } else {
                    Result.success(validMeals.random())
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )



    private fun generateOptions(correctIngredient: String): Result<List<String>> =
        repository.getAllMeals().fold(
            onSuccess = { meals->
                Result.success(getGeneratedOptions(meals, correctIngredient))
            },
            onFailure = {error->
                Result.failure(error)
            }
        )



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
