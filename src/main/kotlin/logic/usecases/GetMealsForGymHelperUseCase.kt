package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.model.GymHelperModel
import org.example.model.FoodChangeMoodExceptions.LogicException.NoMealsForGymHelperException
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
            ?: Result.failure(NoMealsForGymHelperException())
    }

    private fun getGymHelperMeals(
        allMeals: List<Meal>,
        gymHelperModel: GymHelperModel
    ): List<Meal>? {
        return allMeals.filter { meal ->
            meal.nutrition != null && isMealApproximatelyMatchesCaloriesAndProteins(
                meal.nutrition, gymHelperModel
            )
        }.sortedBy { meal ->
            isMealApproximatelyMatchesCaloriesAndProteins(
                meal.nutrition!!, gymHelperModel
            )
        }.takeIf { it.isNotEmpty() }
    }

    private fun handleGetAllMealsFailure(exception: Throwable) =
        Result.failure<List<Meal>>(exception)

    private fun isMealApproximatelyMatchesCaloriesAndProteins(
        nutrition: Nutrition,
        gymHelperModel: GymHelperModel
    ): Boolean {
        return isMealMatchesCalories(
            nutrition,
            desiredCalories = gymHelperModel.desiredCalories,
            approximateAmount = gymHelperModel.approximateAmount
        ) &&
                isMealMatchesProteins(
                    nutrition,
                    desiredProteins = gymHelperModel.desiredProteins,
                    approximateAmount = gymHelperModel.approximateAmount
                )
    }

    private fun isMealMatchesCalories(
        nutrition: Nutrition,
        desiredCalories: Float,
        approximateAmount: Float
    ): Boolean {
        return nutrition.calories?.let {
            isAmountApproximatelyMatches(
                desiredAmount = desiredCalories,
                amount = it,
                approximateAmount = approximateAmount
            )
        } ?: false
    }

    private fun isMealMatchesProteins(
        nutrition: Nutrition,
        desiredProteins: Float,
        approximateAmount: Float
    ): Boolean {
        return nutrition.protein?.let {
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