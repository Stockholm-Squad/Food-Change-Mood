package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions

class GetDessertsWithNoEggsUseCase(private val mealRepository: MealsRepository) {


    fun getDessertsWithNNoEggs(): Result<List<Meal>> {

        return mealRepository.getAllMeals().fold(
            onSuccess = { allMeals -> returnTheFilteredDesserts(allMeals) },
            onFailure = { error -> Result.failure(error) }
        )

    }

    private fun returnTheFilteredDesserts(model: List<Meal>): Result<List<Meal>> {
        return runCatching {
            model.filter { meal ->
                filterForNoEggsAndDesserts(meal.tags, meal.ingredients)
            }.takeIf { it.isNotEmpty() }
                ?: throw FoodChangeMoodExceptions.LogicException.NoDessertFound()
        }

    }

    private fun filterForNoEggsAndDesserts(tags: List<String>?, ingredients: List<String>?): Boolean {
        return !ingredients.toString().contains("egg", ignoreCase = true) &&
                tags.toString().contains("dessert", ignoreCase = true)
    }
}

