package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants

class GetPotatoMealsUseCase(private val mealsRepository: MealsRepository) {

    fun getRandomPotatoMeals(count: Int): Result<List<Meal>> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { allMeals ->
                val potatoMeals = allMeals
                    .filter { meal ->
                        meal.ingredients?.any { it.equals(Constants.POTATO, ignoreCase = true) } == true
                    }
                    .shuffled()
                    .take(count)

                if (potatoMeals.isNotEmpty()) {
                    Result.success(potatoMeals)
                } else {
                    Result.failure(NoSuchElementException(Constants.NO_MEALS_FOR_POTATO))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
