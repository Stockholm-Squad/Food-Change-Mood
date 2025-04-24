package org.example.logic.usecases

import io.mockk.every
import io.mockk.mockk
import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val ketoMeal = Meal(
            name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"), id = 12, contributorId = 5,
            minutes = null,
            submitted = null,
            tags = null,
            nutrition = Nutrition(
               carbohydrates = 5f,
               totalFat = 20f,
               protein = 25f,
               calories = null,
               sugar = null,
               sodium = null,
               saturatedFat = null
            ),
            numberOfSteps = null,
            steps = null,
            description = null,
            numberOfIngredients = null
        )


        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMeal))

        // when
        val result = useCase.getKetoMeal()


        // then
        assertNotNull(result)
        assertEquals(ketoMeal, result)
    }

    @Test
    fun `getAllMeals() should returns null when data of meal are not valid for keto diet`() {
        val nonKetoMeal = Meal(
          name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"), id = 12, contributorId = 5,
          minutes = null,
          submitted = null,
          tags = null,
          nutrition = Nutrition(
              carbohydrates = 50f,
              totalFat = 10f,
              protein = 5f,
              calories = null,
              sugar = null,
              sodium = null,
              saturatedFat = null
          ),
          numberOfSteps = null,
          steps = null,
          description = null,
          numberOfIngredients = null
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(nonKetoMeal))

        //when
        val result = useCase.getKetoMeal()

        //then
        assertNull(result)
    }

    @Test
    fun `getKetoMeal() should not return the same meal twice`() {
        val ketoMeal = Meal(
            name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"), id = 12, contributorId = 5,
            minutes = null,
            submitted = null,
            tags = null,
            nutrition = Nutrition(
                carbohydrates = 5f,
                totalFat = 20f,
                protein = 25f,
                calories = null,
                sugar = null,
                sodium = null,
                saturatedFat = null
            ),
            numberOfSteps = null,
            steps = null,
            description = null,
            numberOfIngredients = null
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(ketoMeal))

        //when
        val firstResult = useCase.getKetoMeal()
        val secondResult = useCase.getKetoMeal()

        //then
        assertEquals(ketoMeal, firstResult)
        assertNull(secondResult)
    }
//
    @Test
    fun `getKetoMeal() should returns null when repository fails`() {
        //given
        every { mealsRepository.getAllMeals() } returns Result.failure(Exception("Something went wrong"))

        //when
        val result = useCase.getKetoMeal()
        //then
        assertNull(result)
    }

    @Test
    fun `getKetoMeal() should ignores meals with null nutrition`() {
        //given
        val mealWithNullNutrition = Meal(
            name = "Spaghetti", ingredients = listOf("Tomato", "Basil", "Garlic"), id = 12, contributorId = 5,
            minutes = null,
            submitted = null,
            tags = null,
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = null,
            numberOfIngredients = null
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(mealWithNullNutrition))

        //when
        val result = useCase.getKetoMeal()

        //then
        assertNull(result)
    }
}
