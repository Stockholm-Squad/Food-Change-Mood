package org.example.logic

import model.Meal
import org.example.data.CsvMealsRepository

class GetMealsByName(
    private val mealsRepository: CsvMealsRepository
) {
    fun getMealsByName(name:String):List<Meal>{
      val allMeals=  mealsRepository.getAllMeals()
      return  searchMealsByNameKMP(allMeals,name)
    }
}