package org.example.presentation

import org.example.logic.model.Results
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase

class ProteinSeafoodRankingUI(private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase) {

    fun proteinSeafoodRanking() {
        when (val results = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()) {
            is Results.Success -> results.model.mapIndexed { index, meal -> println("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition?.protein}") }
            is Results.Fail -> println("No seafood meals were found in the list.h")
        }
    }
}