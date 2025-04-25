package org.example.logic.usecases
import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions.LogicException.NoMealsForSoThinPeopleException
class GetMealForSoThinPeopleUseCase(
    private val mealRepository: MealsRepository,
) {
    fun suggestRandomMealForSoThinPeople(): Result<Meal> {
        return mealRepository.getAllMeals().fold(
            onSuccess = { meals ->
                Result.success(
                    meals.filter { meal ->
                        meal.nutrition?.hasMoreThanMinCalories() == true &&
                                meal.id !in seenMeal
                    }
                        .randomOrNull()
                        ?.also { meal ->
                            seenMeal.add(meal.id)
                        }?: return Result.failure(NoMealsForSoThinPeopleException()))
            },
            onFailure = {Result.failure(NoMealsForSoThinPeopleException()) }
        )

    }

    private fun Nutrition.hasMoreThanMinCalories(): Boolean {
        return (this.calories != null) && (this.calories > MIN_CALORIES)
    }
    companion object {
        private const val MIN_CALORIES = 700
        private val seenMeal: MutableSet<Int> = mutableSetOf()

    }
}
