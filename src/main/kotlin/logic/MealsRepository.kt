package org.example.logic

import model.Meal

//TODO add it to separate package --> ex: repository package

//TODO and make another package to move the useCases on it --> ex: usecases package
interface MealsRepository {
    fun getAllMeals(): List<Meal>
}