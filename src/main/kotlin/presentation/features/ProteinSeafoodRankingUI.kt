package org.example.presentation.features

import org.example.logic.model.FoodChangeModeResults
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase

class ProteinSeafoodRankingUI(private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase) {

    fun proteinSeafoodRanking() {
        when (val results = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()) {
            is FoodChangeModeResults.Success -> results.model.mapIndexed { index, meal -> println("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition?.protein}") }
            is FoodChangeModeResults.Fail -> println("No seafood meals were found in the list.h")
        }
    }
}