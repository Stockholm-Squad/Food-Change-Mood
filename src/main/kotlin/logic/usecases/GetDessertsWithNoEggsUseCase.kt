package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetDessertsWithNoEggsUseCase(private val mealRepository: MealsRepository) {


    fun getDessertsWithNNoEggs(): Result<List<Meal>> {

        return mealRepository.getAllMeals().fold(
            onSuccess = { allMeals -> Result.success(returnTheFilteredDesserts(allMeals)) },
            onFailure = { error -> Result.failure(error) }
        )

    }

    private fun returnTheFilteredDesserts(model: List<Meal>): List<Meal> {
        return model.filter {
            !it.ingredients.toString().contains("egg", ignoreCase = true) && it.tags.toString()
                .contains("dessert", ignoreCase = true)
        }

    }
}

