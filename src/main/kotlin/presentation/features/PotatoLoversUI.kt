package org.example.presentation.features
import model.Meal
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.utils.InputReader
import org.example.utils.OutputPrinter


class PotatoLoversUI(
    private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {

    fun showPotatoLoversUI(count: Int = 10) {
        outputPrinter.printLine(I_LOVE_POTATO_HERE+"$count "+ MEAL_INCLUDE_POTATO+"\n")
        getPotatoMealsUseCase.getRandomPotatoMeals(count)
        askIfWantsMore()
    }


    fun askToViewMealDetails(meals: List<Meal?>) {
        var validInput = false
        do {
            outputPrinter.printLine("\n"+ VIEW_MEAL_DETAILS)

            val input = inputReader.readLineOrNull()!!.trim().lowercase()


            if ( input == "n") {
                outputPrinter.printLine(ENJOY_YOUR_MEAL)
                return
            }

            val selectedIndex = input.toIntOrNull()
            if (selectedIndex != null && selectedIndex in 1..meals.size) {
                meals[selectedIndex - 1]?.let { showMealDetails(it) }
                validInput = true
            } else {
                outputPrinter.printLine(INVALID_SELECTION)
            }
        } while (!validInput)
    }


    fun showMealDetails(meal: Meal) {
        outputPrinter.printLine("\n" + DETAILS_MEAL+ "'${meal.name}':")
        outputPrinter.printLine(MINUTE_TO_PREPARE + "${meal.minutes}")
        outputPrinter.printLine(NUMBER_OF_STEP + "${meal.numberOfSteps}")

        outputPrinter.printLine(STEPS)
        meal.steps?.forEachIndexed { index, step ->
            outputPrinter.printLine("${index + 1}. $step")
        }

        outputPrinter.printLine(DESCRIPTION +"${meal.description}")
        outputPrinter.printLine(NUTRITION+"${meal.nutrition}")
        outputPrinter.printLine(NUMBER_OF_INGREDIENT + " ${meal.numberOfIngredients}")

        outputPrinter.printLine(INGREDIENT)
        meal.ingredients?.forEachIndexed { index, ingredient ->
            outputPrinter.printLine("   ${index + 1}. $ingredient")
        } ?: outputPrinter.printLine(NA)
    }

    fun askIfWantsMore(onYes: () -> Unit = { showPotatoLoversUI() }) {
        outputPrinter.printLine(SEE_MORE_MEALS)
        val input = inputReader.readLineOrNull()

        val normalizedAnswer = normalizeInput(input)

        if (normalizedAnswer == YES) {
            onYes()
        } else {
            outputPrinter.printLine(ENJOY_YOUR_MEAL)
        }
    }


    companion object {
        fun normalizeInput(input: String?): String {
            if (input == null) return ""
            return input.trim().lowercase()
        }
        const val NO_POTATO_MEALS_FOUND = "😢 No potato meals found."
        const val I_LOVE_POTATO = "🥔 I 💛 Potato Meals:"
        const val MEAL = "🍽️ Meal #"
        const val I_LOVE_POTATO_HERE = "🥔 I 💛 Potato! Here are "
        const val MEAL_INCLUDE_POTATO = "meals that include potatoes:"
        const val ERROR = "❌ Error: "
        const val VIEW_MEAL_DETAILS = "Would you like to view the details of any of these meals? (Enter the number or 'n' to skip):"
        const val ENJOY_YOUR_MEAL = "Okay! Enjoy your potato meals! 🥔😋"
        const val INVALID_SELECTION = "Invalid selection. Please choose a valid number."
        const val DETAILS_MEAL = "🍽️ Details of"
        const val MINUTE_TO_PREPARE = "🕒 Minutes to prepare:"
        const val NUMBER_OF_STEP = "📖 Number of steps:"
        const val STEPS = "📝 Steps:"
        const val DESCRIPTION = "📃 Description:"
        const val NUTRITION = "🍎 Nutrition"
        const val NUMBER_OF_INGREDIENT = "🥣 Number of ingredients:"
        const val INGREDIENT = "🧂 Ingredients:"
        const val NA = "   N/A"
        const val SEE_MORE_MEALS = "Would you like to see more? (y/n)"
        const val YES = "y"
    }


}