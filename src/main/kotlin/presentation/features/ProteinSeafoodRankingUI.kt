package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase
import org.example.model.FoodChangeMoodExceptions

class ProteinSeafoodRankingUI(private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase) {

    fun proteinSeafoodRanking() {
        getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank().fold(
            onSuccess = { handleSuccess(it) },
            onFailure = { handleFailure(it.message) })
    }

    private fun handleSuccess(meals: List<Meal>) {
        meals.mapIndexed { index, meal -> println("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition?.protein}") }

    }

    private fun handleFailure( exceptionMessage: String?) {
        println(exceptionMessage)
    }
}