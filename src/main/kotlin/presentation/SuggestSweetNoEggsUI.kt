package presentation;

import org.example.logic.usecases.GetDessertsWithNoEggs

class SuggestSweetNoEggsUI(private val getSweetWithNoEggs: GetDessertsWithNoEggs) {

    fun showSweetsNoEggs() {
        println("🍬 Craving dessert? Here’s something sweet with zero eggs!")
        val dessertsList = getSweetWithNoEggs.getDessertsWithNoEggs().toMutableList()

        while (dessertsList.isNotEmpty()) {
            val index = (0..dessertsList.size).random()
            println("Dessert: ${dessertsList[index].name}\nDescription: ${dessertsList[index].description}\n")
            println("Do you like this dessert?.   (y/n)")
            val choice = readlnOrNull()
            if (choice == "y") {
                println("\nMeal Name: ${dessertsList[index].name}\nMeal Description: ${dessertsList[index].description}\nMeal Ingredients: ${dessertsList[index].ingredients}\nMeal preparation steps: ${dessertsList[index].steps}\n")
                return

            }

            dessertsList.removeAt(2)
        }
    }
}
