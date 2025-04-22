package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository

class GetCountryFoodUseCase(private val mealRepository: MealsRepository) {
    fun getRandomMealsForCountry(countryName: String): Results<List<Meal>> {

        return when (val result = mealRepository.getAllMeals()) {
            is Results.Success -> result.model.filter {
                it.name?.contains(countryName, ignoreCase = true) ?: false
                        || it.tags?.contains(countryName) ?: false
            }.ifEmpty { throw NoSuchElementException("No meals found for country: $countryName") }
                .shuffled().take(20).let { Results.Success(it) }

            is Results.Fail -> Results.Fail(result.exception)
        }


    }

}