package data

import model.Meal

interface MealRepository {
    fun getAllMeals():List<Meal>
}