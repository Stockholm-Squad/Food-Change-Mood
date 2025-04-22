package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetCountryMealsUseCase(private val mealRepository: MealsRepository) {
    fun getMealsForCountry(countryName: String): Result<List<Meal>> {

        return mealRepository.getAllMeals().fold(
            onSuccess = { allMeals -> Result.success(returnRandomMealsByCountry(allMeals, countryName)) },
            onFailure = { error -> Result.failure(error) },
        )

    }

    private fun returnRandomMealsByCountry(allMeals: List<Meal>, countryName: String): List<Meal> {
        return allMeals.filter {
            it.name?.contains(countryName, ignoreCase = true) ?: false
                    || it.tags?.contains(countryName) ?: false
        }.shuffled().take(20)
    }
}