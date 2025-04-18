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
                it.nutrition?.isMealApproxmatlyMatchesCaloriesAndProteins(
                    desiredCalories = desiredCalories,
                    desiredProteins = desiredProteins,
                    approximateAmount = approximateAmount
                ) ?: false
            }
            .takeIf { it.isNotEmpty() }
            ?.sortedBy {
                it.nutrition.sortMealByProteinsAndCalories(
                    desiredCalories = desiredCalories,
                    desiredProteins = desiredProteins,
                    approximateAmount = approximateAmount
                )
            } ?: throw Throwable(message = Messages.NO_MEALS_FOR_GYM_HELPER.messages)
    }

    private fun Nutrition.sortMealByProteinsAndCalories(
        desiredCalories: Float,
        desiredProteins: Float,
        approximateAmount: Float
    ): Boolean {
        return this.isMealApproxmatlyMatchesCaloriesAndProteins(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins,
            approximateAmount = approximateAmount
        )
    }

    private fun Nutrition.isMealApproxmatlyMatchesCaloriesAndProteins(
        desiredCalories: Float,
        desiredProteins: Float,
        approximateAmount: Float
    ): Boolean {
        return this.isMealMatchesCalories(desiredCalories = desiredCalories, approximateAmount = approximateAmount) &&
                this.isMealMatchesProteins(desiredProteins = desiredProteins, approximateAmount = approximateAmount)
    }

    private fun Nutrition.isMealMatchesCalories(
        desiredCalories: Float,
        approximateAmount: Float
    ): Boolean {
        return this.calories?.let {
            isAmountApproximatlyMatches(
                desiredAmount = desiredCalories,
                amount = it,
                approximateAmount = approximateAmount
            )
        } ?: false
    }

    private fun Nutrition.isMealMatchesProteins(
        desiredProteins: Float,
        approximateAmount: Float
    ): Boolean {
        return this.protein?.let {
            isAmountApproximatlyMatches(
                desiredAmount = desiredProteins,
                amount = it,
                approximateAmount = approximateAmount
            )
        } ?: false
    }

    private fun isAmountApproximatlyMatches(
        desiredAmount: Float,
        amount: Float,
        approximateAmount: Float
    ) = ((abs(desiredAmount - amount) * 100) / desiredAmount) <= approximateAmount
}