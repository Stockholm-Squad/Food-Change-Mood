package org.example.utils

object Constants {
    const val MEAL_CSV_FILE = "food.csv"
    const val INVALID_INPUT = "Invalid input"
    const val NO_MEALS_FOR_GYM_HELPER = "No meals match the desired protein and calories!!"
    const val NO_MEALS_FOR_POTATO = "No potato meals found."
    const val POTATO = "potato"
    const val SEARCH_QUERY_CAN_NOT_BE_EMPTY = "Search query cannot be empty or null"
    const val NO_MEALS_FOUND_MATCHING = "No meals found matching"
    const val UNEXPECTED_ERROR = "Unexpected error occurred"
    const val ERROR_FETCHING_MEALS = "Error fetching meals: "
    const val ASK_YES_NO = "Do you like it ? (yes/no)"
    const val YES_ANSWER = "Great, enjoy"


    // ingredient game constants
    const val WELCOME_INGREDIENTS_GAME_MESSAGE = "Ingredient Game is starting! Let's see how well you know your meals!" +
                                                "\nGuess the correct ingredient for each meal." +
                                                "\nGet 15 right to win! One wrong answer ends the game."+
                                                "\n--------------------------------------------------"

    const val WINNING_MESSAGE = "Congratulations! You reached the winning score: "
    const val CORRECT_CHOICE = "✅ Correct! Your current score: "
    const val INCORRECT_MESSAGE = "Game Over! Final Score: "

    // keto diet messages
    const val START_MESSAGE = "Finding a keto-friendly meal for you..."
    const val FINISH_MEALS = "✔You've seen all available keto meals!"

}