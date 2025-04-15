package org.example.data

import model.Meal
import org.example.logic.IMealsRepository
import java.util.Collections.emptyList

class MealsRepository() : IMealsRepository {
    override fun getAllMeals(): List<Meal> {
        //  TODO("Not yet implemented")
        return emptyList()
    }
}