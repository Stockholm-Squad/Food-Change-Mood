package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository

class GetDessertsWithNoEggsUseCase(private val mealRepository: MealsRepository) {


    fun getDessertsWithNNoEggs(): Results<List<Meal>> {

        return when (val result = mealRepository.getAllMeals()) {
            is Results.Success -> result.model.filter {
                !it.ingredients.toString().contains("egg") && it.tags.toString().contains("dessert")
            }.let { Results.Success(it) }

            is Results.Fail -> Results.Fail(result.exception)
        }
    }


}

