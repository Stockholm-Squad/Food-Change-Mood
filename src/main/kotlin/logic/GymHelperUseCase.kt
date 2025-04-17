package logic

import model.Meal
import model.Nutrition
import org.example.logic.MealsRepository
import org.example.utils.Messages
import kotlin.math.abs

class GymHelperUseCase(
    private val mealRepository: MealsRepository
) {
    fun getGymHelperMeals(
        desiredCalories: Float,
        desiredProteins: Float,
        approximateAmount: Float = 20F
    ): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {
                it.nutrition.isMealsClosedToDesiredCaloriesAndProteins(
                    desiredCalories = desiredCalories,
                    desiredProteins = desiredProteins,
                    approximateAmount = approximateAmount
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.sortedBy {
                it.nutrition.sortByProteinsAndCalories(
                    desiredCalories = desiredCalories,
                    desiredProteins = desiredProteins,
                    approximateAmount = approximateAmount
                )
            } ?: throw Throwable(message = Messages.NO_MEALS_FOR_GYM_HELPER.messages)
    }

    private fun Nutrition.sortByProteinsAndCalories(
        desiredCalories: Float,
        desiredProteins: Float,
        approximateAmount: Float
    ): Boolean {
        return this.isMealsClosedToDesiredCaloriesAndProteins(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins,
            approximateAmount = approximateAmount
        )
    }

    private fun Nutrition.isMealsClosedToDesiredCaloriesAndProteins(
        desiredCalories: Float,
        desiredProteins: Float,
        approximateAmount: Float
    ): Boolean {
        return calories.let { caloriesAmount ->
            protein.let { proteinsAmount ->
                isClosedToDesiredAmount(desiredCalories, caloriesAmount, approximateAmount)
                        && isClosedToDesiredAmount(desiredProteins, proteinsAmount, approximateAmount)
            }
        }
    }

    private fun isClosedToDesiredAmount(
        desiredAmount: Float,
        amount: Float,
        approximateAmount: Float
    ) = ((abs(desiredAmount - amount) * 100) / desiredAmount) <= approximateAmount
}