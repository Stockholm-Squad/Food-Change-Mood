package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.presentation.features.PotatoLoversUI.Companion.ERROR
import org.example.presentation.features.PotatoLoversUI.Companion.I_LOVE_POTATO
import org.example.presentation.features.PotatoLoversUI.Companion.MEAL
import org.example.presentation.features.PotatoLoversUI.Companion.NO_POTATO_MEALS_FOUND
import org.example.utils.Constants

class GetPotatoMealsUseCase(private val mealsRepository: MealsRepository) {

    fun getRandomPotatoMeals(count: Int): Result<List<Meal>> {
        return count.takeIf { it > 0 }?.let {
            mealsRepository.getAllMeals().fold(
                onSuccess = { allMeals ->
                    allMeals
                        .filter { meal -> meal.ingredients?.any { it.equals(Constants.POTATO, ignoreCase = true) } == true }
                        .shuffled()
                        .take(count)
                        .takeIf { it.isNotEmpty() }
                        ?.let { Result.success(it) }
                        ?: Result.failure(Throwable(Constants.NO_MEALS_FOR_POTATO))
                },
                onFailure = {
                    Result.failure(Throwable("${Constants.UNEXPECTED_ERROR} ${this::class.simpleName}"))
                }
            )
        } ?: Result.success(emptyList())
    }
}

