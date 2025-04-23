package org.example.input_output.output

import model.Meal

class OutputPrinterImplementation : OutputPrinter {
    override fun printLine(value: String?) {
        println(value)
    }

    override fun printMeals(allMeals: List<Meal>?) {
        allMeals?.forEachIndexed { index, meal ->
            println("----------------------------------------")
            println("Meal ${index + 1}:\n")
            this.printMealsDetails(meal)
            println("----------------------------------------")
        }
    }

    override fun printMeal(meal: Meal?) {
        println("Meal: \n")

    }

    private fun printMealsDetails(meal: Meal?) {
        println(
            "Name = '${meal?.name}', " +
                    "ID = ${meal?.id}, " +
                    "Minutes = ${meal?.minutes}\n" +
                    "ContributorID = ${meal?.contributorId}, " +
                    "Submitted = '${meal?.submitted}'\n" +
                    "Tags = ${meal?.tags}\n" +
                    "Nutrition = ${meal?.nutrition}\n" +
                    "StepsCount = ${meal?.numberOfSteps}\n" +
                    "Steps = ${meal?.steps}\n" +
                    "Description = '${meal?.description?.take(30)}...'\n" +
                    "Ingredients = ${meal?.ingredients}\n" +
                    "IngredientsCount = ${meal?.numberOfIngredients}\n".trimIndent()
        )
    }

}