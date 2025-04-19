package org.example.presentation

import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase

class ProteinSeafoodRankingUI(private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase) {

    fun proteinSeafoodRanking() {
        try {
            getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
                .mapIndexed { index, meal -> println("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition.protein}") }
        } catch (exception: NoSuchElementException) {
            println("No seafood meals were found in the list.")
        }

    }
}