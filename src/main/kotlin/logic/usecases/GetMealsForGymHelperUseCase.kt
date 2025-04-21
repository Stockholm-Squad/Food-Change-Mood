package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants
import kotlin.math.abs

class GetMealsForGymHelperUseCase(
    private val mealRepository: MealsRepository
) {
    private val gymHelperModel = GymHelperModel()

    fun getGymHelperMeals(
        desiredCalories: Float,
        desiredProteins: Float,
        approximateAmount: Float = 20F
    ): Results<List<Meal>> {
        initGymHelperModel(desiredCalories, desiredProteins, approximateAmount).also {
            return mealRepository.getAllMeals().apply {
                when (this) {
                    is Results.Fail -> handleGetAllMealsFailure()
                    is Results.Success -> handleGetAllMealsSuccess(
                        this.model,
                        this@GetMealsForGymHelperUseCase.gymHelperModel
                    )
                }
            }
        }
    }

    private fun initGymHelperModel(desiredCalories: Float, desiredProteins: Float, approximateAmount: Float) {
        this.gymHelperModel.apply {
            this.desiredCalories = desiredCalories
            this.desiredProteins = desiredProteins
            this.approximateAmount = approximateAmount
        }
    }

    private fun handleGetAllMealsSuccess(allMeals: List<Meal>, gymHelperModel: GymHelperModel): Results<List<Meal>> {
        return getGymHelperMeals(allMeals, gymHelperModel)?.let {
            Results.Success(it)
        }
            ?: Results.Fail(Throwable(message = Constants.NO_MEALS_FOR_GYM_HELPER))
    }

    private fun getGymHelperMeals(
        allMeals: List<Meal>,
        gymHelperModel: GymHelperModel
    ): List<Meal>? {
        return allMeals.filter {
            it.nutrition?.isMealApproxmatlyMatchesCaloriesAndProteins(
                gymHelperModel
            ) ?: false
        }
            .takeIf { it.isNotEmpty() }
            ?.sortedBy {
                it.nutrition
                    ?.sortMealByProteinsAndCalories(
                        gymHelperModel
                    )
            }
    }

    private fun handleGetAllMealsFailure() = Results.Fail(Throwable(Constants.NO_MEALS_FOR_GYM_HELPER))

    private fun Nutrition.sortMealByProteinsAndCalories(
        gymHelperModel: GymHelperModel
    ): Boolean {
        return this.isMealApproxmatlyMatchesCaloriesAndProteins(
            gymHelperModel
        )
    }

    private fun Nutrition.isMealApproxmatlyMatchesCaloriesAndProteins(gymHelperModel: GymHelperModel): Boolean {
        return this.isMealMatchesCalories(
            desiredCalories = gymHelperModel.desiredCalories,
            approximateAmount = gymHelperModel.approximateAmount
        ) &&
                this.isMealMatchesProteins(
                    desiredProteins = gymHelperModel.desiredProteins,
                    approximateAmount = gymHelperModel.approximateAmount
                )
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


data class GymHelperModel(
    var desiredCalories: Float = 0.0F,
    var desiredProteins: Float = 0.0F,
    var approximateAmount: Float = 20F
)