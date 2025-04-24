package org.example.logic.usecases

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal
import utils.buildNutrition

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
            name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"),
            id = 12,
            contributorId = 5,
            nutrition = buildNutrition(
               carbohydrates = 5f,
               totalFat = 20f,
               protein = 25f,
            ),
        )


        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMeal))

        // when
        val result = useCase.getKetoMeal()


        // then
        assertNotNull(result.getOrNull())
        assertEquals(ketoMeal, result.getOrNull())
    }

    @Test
    fun `getAllMeals() should returns null when data of meal are not valid for keto diet`() {
        val nonKetoMeal = buildMeal(
          name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"), id = 12, contributorId = 5,
          nutrition = buildNutrition(
              carbohydrates = 50f,
              totalFat = 10f,
              protein = 5f,
          ),
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(nonKetoMeal))

        //when
        val result = useCase.getKetoMeal()

        //then
        assertNull(result.getOrNull())
    }

    @Test
    fun `getKetoMeal() should not return the same meal twice`() {
        val ketoMeal = buildMeal(
            name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"), id = 12, contributorId = 5,
            nutrition = buildNutrition(
                carbohydrates = 5f,
                totalFat = 20f,
                protein = 25f,
            ),
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMeal))

        //when
        val firstResult = useCase.getKetoMeal()
        val secondResult = useCase.getKetoMeal()

        //then
        assertEquals(ketoMeal, firstResult.getOrNull())
        assertNull(secondResult.getOrNull())
    }
//
    @Test
    fun `getKetoMeal() should returns null when repository fails`() {
        //given
        every { mealsRepository.getAllMeals() } returns Result.failure(Exception("Something went wrong"))

        //when
        val result = useCase.getKetoMeal()
        //then
        assertNull(result.getOrNull())
    }

    @Test
    fun `getKetoMeal() should ignores meals with null nutrition`() {
        //given
        val mealWithNullNutrition = buildMeal(
            name = "Spaghetti",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            id = 12,
            contributorId = 5,
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(mealWithNullNutrition))

        //when
        val result = useCase.getKetoMeal()

        //then
        assertNull(result.getOrNull())
    }
}
