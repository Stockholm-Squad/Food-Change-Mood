package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetCountryFoodUseCase(private val mealRepository: MealsRepository) {
    fun getRandomMealsForCountry(countryName: String): List<Meal> {
        return try {
            val meals = mealRepository.getAllMeals()
            meals.filter {
                it.name?.contains(countryName, ignoreCase = true) ?: false
                        || it.tags?.contains(countryName) ?: false
            }.ifEmpty { throw NoSuchElementException("No meals found for country: $countryName") }
                .shuffled().take(20)
        } catch (e: Exception) {
            emptyList()
        }
    }

}

