package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions

class GetCountryMealsUseCase(private val mealRepository: MealsRepository) {
    fun getMealsForCountry(countryName: String): Result<List<Meal>> {

        return mealRepository.getAllMeals().fold(
            onSuccess = { allMeals -> returnRandomMealsByCountry(allMeals, countryName) },
            onFailure = { error -> Result.failure(error) },
        )

    }

    private fun returnRandomMealsByCountry(allMeals: List<Meal>, countryName: String): Result<List<Meal>> {
        return runCatching {
            allMeals.filter {
                filterForNameAndTags(it.name.toString(), it.tags, countryName)
            }.shuffled().take(20).takeIf { it.isNotEmpty() && countryName.isNotEmpty() }
                ?: throw FoodChangeMoodExceptions.LogicException.NoMealsFoundForCountry()
        }
    }

    private fun filterForNameAndTags(name: String, tags: List<String>?, countryName: String): Boolean {
        return name.contains(countryName, ignoreCase = true) ||
                tags.toString().contains(countryName, ignoreCase = true)
    }

}