package presentation;

import org.example.logic.usecases.GetMealForKetoDietUseCase

class KetoDietMealUI(private val getMealForKetoDietUseCase: GetMealForKetoDietUseCase) {

    fun showKetoMeal() {
        println("Finding keto-friendly meal for you...")
        val meal = getMealForKetoDietUseCase.getKetoMeal()

        if (meal == null) {
            println("You've seen all available keto meals!")
            return
        }

        println("Suggested Keto Meal: ${meal.name}")
        println("Description: ${meal.description}")
        println("Nutrition -> Carbs: ${meal.nutrition?.carbohydrates}g, Fat: ${meal.nutrition?.totalFat}g, Protein: ${meal.nutrition?.protein}g")

        println("\nDo you like this meal? (yes/no)")
        when (readln().trim().lowercase()) {
            "yes" -> {
                println("Great, Enjoy your meal: ${meal.name}")
            }
            "no" -> {
                showKetoMeal()
            }
            else -> {
                println("Invalid input. Please answer with 'yes' or 'no'.")
                showKetoMeal()
            }
        }
    }
}

