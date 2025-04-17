package org.example.presentation

import org.example.logic.GetSeaFoodByProteinRankUseCase

class ProteinSeafoodRankingUI(private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase) {

    fun proteinSeafoodRanking() {
        getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
            .mapIndexed { index, meal -> println("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition.protein}") }
    }
}