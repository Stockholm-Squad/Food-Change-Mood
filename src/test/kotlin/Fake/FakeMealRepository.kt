package Fake

import model.Meal
import org.example.logic.repository.MealsRepository

class FakeMealRepository : MealsRepository {
    override fun getAllMeals(): Result<List<Meal>> {
        TODO("Not yet implemented")
    }
}