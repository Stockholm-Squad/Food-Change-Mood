package org.example.logic.usecases

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal
import utils.buildNutrition
import com.google.common.truth.Truth

class GetMealForKetoDietUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var useCase: GetMealForKetoDietUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk()
        useCase = GetMealForKetoDietUseCase(mealsRepository)
    }

    @Test
    fun `getKetoMeal() should returns a keto meal when data of meal are valid for keto diet`() {
        // given
        val ketoMeal = buildMeal(
            id = 12,
            name = "Spaghetti",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            contributorId = 5,
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = 20f,
                protein = 25f
            )
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMeal))

        // when
        val result = useCase.getKetoMeal()


        // then
        Truth.assertThat(result.getOrNull()).isNotNull()
        Truth.assertThat(ketoMeal).isEqualTo(result.getOrNull())
    }

    @Test
    fun `getAllMeals() should returns null when data of meal are not valid for keto diet`() {
        val nonKetoMeal = buildMeal(
            id = 12,
            name = "Pasta Primavera",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            nutrition = buildNutrition(
                carbohydrates = 50f,
                totalFat = 10f,
                protein = 5f
            )
        )

        val secondNonKetoMeal = nonKetoMeal.copy(
            name = "Vegetable Stir Fry",
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = 10f,
                protein = 5f
            )
        )

        val thirdNonKetoMeal = nonKetoMeal.copy(
            name = "Grilled Chicken Salad",
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = 16f,
                protein = 5f
            )
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(nonKetoMeal, secondNonKetoMeal, thirdNonKetoMeal))

        //when
        val result = useCase.getKetoMeal()
        val result2 = useCase.getKetoMeal()
        val result3 = useCase.getKetoMeal()

        //then
        Truth.assertThat(result.getOrNull()).isNull()
        Truth.assertThat(result2.getOrNull()).isNull()
        Truth.assertThat(result3.getOrNull()).isNull()
    }

    @Test
    fun `getKetoMeal() should not return the same meal twice`() {
        val ketoMeal = buildMeal(
            id = 12,
            name = "Spaghetti",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            contributorId = 5,
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = 20f,
                protein = 25f
            )
        )


        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMeal))

        //when
        val firstResult = useCase.getKetoMeal()
        val secondResult = useCase.getKetoMeal()

        //then
        Truth.assertThat(firstResult.getOrNull()).isEqualTo(ketoMeal)
        Truth.assertThat(secondResult.getOrNull()).isNull()
    }
//
    @Test
    fun `getKetoMeal() should returns null when repository fails`() {
        //given
        every { mealsRepository.getAllMeals() } returns Result.failure(Exception("Something went wrong"))

        //when
        val result = useCase.getKetoMeal()
        //then
        Truth.assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `getKetoMeal() should ignores meals with null nutrition`() {
        //given
        val mealWithNullNutrition = buildMeal(
            id = 12,
            name = "Spaghetti",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            contributorId = 5,
            nutrition = null
        )


        every { mealsRepository.getAllMeals() } returns Result.success(listOf(mealWithNullNutrition))

        //when
        val result = useCase.getKetoMeal()

        //then
        Truth.assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `getKetoMeal() should not return the same meal twice when nutrition values are null`() {
        // Given
        val ketoMealWithNullCarbo = buildMeal(
            id = 12,
            name = "Spaghetti",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            contributorId = 5,
            nutrition = buildNutrition(
                carbohydrates = null,
                totalFat = 16f,
                protein = 20f
            )
        )

        val ketoMealWithNullFat = ketoMealWithNullCarbo.copy(
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = null,
                protein = 20f
            )
        )

        val ketoMealWithNullProtein = ketoMealWithNullCarbo.copy(
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = 16f,
                protein = null
            )
        )


        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMealWithNullCarbo , ketoMealWithNullFat, ketoMealWithNullProtein))

        // When
        val result = useCase.getKetoMeal()
        val result2 = useCase.getKetoMeal()
        val result3 = useCase.getKetoMeal()

        // Then
        Truth.assertThat(result.getOrNull()).isNull()
        Truth.assertThat(result2.getOrNull()).isNull()
        Truth.assertThat(result3.getOrNull()).isNull()
    }

}
