package org.example.logic.repo

import model.Meal

interface IMealRepository {


    fun getAllMeals():List<Meal>
}