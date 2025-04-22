package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.model.GymHelperModel
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
    ): Result<List<Meal>> {
        initGymHelperModel(desiredCalories, desiredProteins, approximateAmount).also {
            return mealRepository.getAllMeals().fold(
                onSuccess = { allMeals ->
                    handleGetAllMealsSuccess(
                        allMeals,
                        this@GetMealsForGymHelperUseCase.gymHelperModel
                    )
                },
                onFailure = { exception -> handleGetAllMealsFailure(exception) }
            )
        }
    }

    private fun initGymHelperModel(desiredCalories: Float, desiredProteins: Float, approximateAmount: Float) {
        this.gymHelperModel.apply {
            this.desiredCalories = desiredCalories
            this.desiredProteins = desiredProteins
            this.approximateAmount = approximateAmount
        }
    }

    private fun handleGetAllMealsSuccess(allMeals: List<Meal>, gymHelperModel: GymHelperModel): Result<List<Meal>> {
        return getGymHelperMeals(allMeals, gymHelperModel)?.let {
            Result.success(it)
        }
            ?: Result.failure(Throwable(message = Constants.NO_MEALS_FOR_GYM_HELPER))
    }

    private fun getGymHelperMeals(
        allMeals: List<Meal>,
        gymHelperModel: GymHelperModel
    ): List<Meal>? {
        return allMeals.filter {
            it.nutrition?.isMealApproximatelyMatchesCaloriesAndProteins(
                gymHelperModel
            ) ?: false
        }
            .takeIf { it.isNotEmpty() }
            ?.sortedBy {
                it.nutrition
                    ?.isMealApproximatelyMatchesCaloriesAndProteins(
                        gymHelperModel
                    )
            }
    }

    private fun handleGetAllMealsFailure(exception: Throwable) =
        Result.failure<List<Meal>>(Throwable(Constants.NO_MEALS_FOR_GYM_HELPER + "\n" + exception.message))

    private fun Nutrition.isMealApproximatelyMatchesCaloriesAndProteins(gymHelperModel: GymHelperModel): Boolean {
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
            isAmountApproximatelyMatches(
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
            isAmountApproximatelyMatches(
                desiredAmount = desiredProteins,
                amount = it,
                approximateAmount = approximateAmount
            )
        } ?: false
    }

    private fun isAmountApproximatelyMatches(
        desiredAmount: Float,
        amount: Float,
        approximateAmount: Float
    ) = ((abs(desiredAmount - amount) * 100) / desiredAmount) <= approximateAmount
}