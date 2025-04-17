package org.example.presentation

import data.FoodCsvParser
import data.FoodCsvReader
import model.Meal
import org.example.data.FoodCsvRepository
import org.example.logic.ItalianMealsForLargeGroupUseCase
import org.example.logic.MealsRepository
import java.io.File

class ItalianLargeGroupMealsUI {
    private val repository: MealsRepository = FoodCsvRepository(
        foodCsvReader = FoodCsvReader(
            csvFile = File("food.csv")
        ),

        foodCsvParser = FoodCsvParser()
    )

    private val useCase = ItalianMealsForLargeGroupUseCase(repository)

    fun italianLargeGroupMealsUI() {
        println("🍝 Planning a big Italian feast? Here's a list of meals perfect for large groups:")
        println("Loading...")
        val filteredList = useCase.getItalianMealsForLargeGroup()
        filteredList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }

        while (true) {
            println()
            println("-1 -> back")
            println("meal id -> view details")
            val input = readlnOrNull()
            val mealId = input?.toIntOrNull()

            if (mealId == null) {
                println("Enter a valid ID or -1")
                continue
            } else if (mealId == -1) {
                break
            } else {
                viewMealDetails(filteredList, mealId)
            }
        }
    }

    private fun viewMealDetails(mealsList: List<Meal>, mealId: Int) {
        val meal: Meal? = useCase.getMeal(mealsList, mealId)

        if (meal == null) {
            println("The meal with ID $mealId does not exist.")
            return
        }

        println(meal)
    }

}