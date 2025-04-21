package org.example.presentation.features

import model.Meal
import org.example.logic.model.Results
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.utils.Constants

class GymHelperUI(
    private val getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase?
) {

    fun useGymHelper() {
        this.getDesiredCalories()?.let { desiredCalories ->
            this@GymHelperUI.getDesiredProteins()?.let { desiredProteins ->
                getMealsForGymHelperUseCase?.getGymHelperMeals(
                    desiredCalories = desiredCalories,
                    desiredProteins = desiredProteins
                ).apply {
                    when (this) {
                        is Results.Fail -> this@GymHelperUI.handleFailure(this.exception)
                        is Results.Success<List<Meal>> -> this@GymHelperUI.handleSuccess(this.model)
                        null -> handleUnExpectedError()
                    }
                }
            } ?: showInvalidInput()
        } ?: showInvalidInput()
    }

    private fun getDesiredProteins(): Float? {
        print("🔥 Enter desired proteins: ")
        return readlnOrNull()?.toFloatOrNull()
    }

    private fun getDesiredCalories(): Float? {
        print("🔥 Enter desired calories: ")
        return readlnOrNull()?.toFloatOrNull()
    }

    private fun handleFailure(exception: Throwable) {
        println(exception.message)
    }

    private fun handleSuccess(gymHelperMeals: List<Meal>) {
        gymHelperMeals.forEach {
            println(it)
        }
    }

    private fun handleUnExpectedError() {
        this@GymHelperUI.handleFailure(Throwable("UnExpected Error ${GymHelperUI::class.simpleName}"))
    }

    private fun showInvalidInput() {
        println(Constants.INVALID_INPUT)
    }

}