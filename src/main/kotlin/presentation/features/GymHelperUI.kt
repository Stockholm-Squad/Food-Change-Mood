package org.example.presentation.features

import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.utils.Constants

class GymHelperUI(
    private val getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase?
) {

    fun useGymHelper() {
        print("🔥 Enter desired calories: ")
        val desiredCalories = readlnOrNull()?.toFloatOrNull()
        if (desiredCalories == null) {
            println(Constants.INVALID_INPUT)
            return
        }

        print("🔥 Enter desired proteins: ")
        val desiredProteins = readlnOrNull()?.toFloatOrNull()

        if (desiredProteins == null) {
            println(Constants.INVALID_INPUT)
            return
        }

        try {
            getMealsForGymHelperUseCase?.getGymHelperMeals(
                desiredCalories = desiredCalories,
                desiredProteins = desiredProteins
            )?.takeIf {
                it.isNotEmpty()
            }?.forEach {
                println(it)
            } ?: println(Constants.NO_MEALS_FOR_GYM_HELPER)

        } catch (throwable: Throwable) {
            println(throwable.message)
        }

    }

}