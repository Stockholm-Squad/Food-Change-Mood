package org.example.presentation.features

import model.Meal
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase

class ProteinSeafoodRankingUI(private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase,private val printer: OutputPrinter) {

    fun proteinSeafoodRanking() {
        getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank().fold(
            onSuccess = { handleSuccess(it) },
            onFailure = { handleFailure(it.message) })
    }

    private fun handleSuccess(meals: List<Meal>) {
        meals.mapIndexed { index, meal -> printer.printLine("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition!!.protein}") }

    }

    private fun handleFailure( exceptionMessage: String?) {
        printer.printLine(exceptionMessage)
    }
}