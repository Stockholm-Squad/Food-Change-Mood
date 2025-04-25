package logic.usecases

import Fake.createMeal
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetPotatoMealsUseCaseTest{

    private lateinit var mealRepository : MealsRepository
    private lateinit var getRandomPotatoMeals : GetPotatoMealsUseCase

  @BeforeEach
  fun setUp() {
      mealRepository = mockk(relaxed = true)
      getRandomPotatoMeals = GetPotatoMealsUseCase(mealRepository)
  }
    @Test
    fun `getRandomPotatoMeals should return potato meals when valid input`() {
        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Potato Salad", ingredients = listOf("Potato", "Mayonnaise", "Onion")),
            buildMeal(2, "Mashed Potatoes", ingredients = listOf("Potato", "Butter", "Milk")),
            buildMeal(3, "French Fries", ingredients = listOf("Potato", "Oil", "Salt")),
            buildMeal(4, "Tomato Soup", ingredients = listOf("Tomato", "Water", "Salt"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = 5
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        val potatoMeal = result.getOrNull()
        assertThat(potatoMeal).isNotNull()
        assertThat(potatoMeal!!.size).isEqualTo(3)
        potatoMeal.forEach { meal ->
            assertThat(meal.ingredients?.any { it.equals("potato", ignoreCase = true) }).isTrue()
        }
        verify(exactly = 1) { mealRepository.getAllMeals() }
    }
    @Test
    fun `getRandomPotatoMeals should return all available potato meals if count exceeds available`() {

        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Potato Salad", ingredients = listOf("Potato", "Mayonnaise", "Onion")),
            buildMeal(2, "Mashed Potatoes", ingredients = listOf("Potato", "Butter", "Milk"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = 10
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(2)
    }

    @Test
    fun `getRandomPotatoMeals should return failure when no meals contain potato`() {

        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Avocado Toast", ingredients = listOf("Bread", "Avocado", "Salt", "Pepper", "Lemon juice")),
            buildMeal(2, "Chicken Curry", ingredients = listOf("Chicken", "Onion", "Garlic", "Ginger", "Spices"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()!!.message).isEqualTo(Constants.NO_MEALS_FOR_POTATO)
        verify(exactly = 1) { mealRepository.getAllMeals() }
    }

    @Test
    fun `getRandomPotatoMeals should return failure when repository fails`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.failure(Exception("Repository Error"))

        // When
        val inputCount = 2
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        val isFailure = result.isFailure
        val exceptionMessage = result.exceptionOrNull()?.message

        assertThat(isFailure).isTrue()
        assertThat(exceptionMessage).contains(Constants.UNEXPECTED_ERROR)
    }

    @Test
    fun `getRandomPotatoMeals should handle case ignore potato filter`() {

        //Given
        val meals = listOfNotNull(
            buildMeal(1, "POTATO FRIES", ingredients = listOf("POTATO")),
            buildMeal(2, "Mashed", ingredients = listOf("potato")),
            buildMeal(3, "Mixed", ingredients = listOf("PoTaTo"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(3)
        verify(exactly = 1) { mealRepository.getAllMeals() }
    }

    @Test
    fun `getRandomPotatoMeals should return empty list when count is zero`() {

        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Potato Salad", ingredients = listOf("Potato", "Mayonnaise", "Onion")),
            buildMeal(2, "Mashed Potatoes", ingredients = listOf("Potato", "Butter", "Milk")),
            buildMeal(3, "French Fries", ingredients = listOf("Potato", "Oil", "Salt")),
            buildMeal(4, "Tomato Soup", ingredients = listOf("Tomato", "Water", "Salt"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = 0
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
        verify(exactly = 0) { mealRepository.getAllMeals() }
    }

    @Test
    fun `getRandomPotatoMeals should skip meals with null ingredients`() {
        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Potato Salad", ingredients = listOf("Potato", "Mayonnaise", "Onion")),
            buildMeal(2, "Mashed Potatoes", ingredients = listOf("Potato", "Butter", "Milk")),
            buildMeal(3, "Tomato Soup", ingredients = null),
            buildMeal(4, "French Fries", ingredients = listOf("Potato", "Oil", "Salt")),
            buildMeal(5, "Chicken Soup", ingredients = null)
        )

        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = 5
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        val mealsResult = result.getOrNull()
        assertThat(mealsResult!!.size).isEqualTo(3)
        mealsResult.forEach { meal ->
            assertThat(meal.ingredients).isNotNull()
        }
        verify(exactly = 1) { mealRepository.getAllMeals() }
    }

    @Test
    fun `getRandomPotatoMeals should return empty list when count is negative`() {

        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Potato Salad", ingredients = listOf("Potato", "Mayonnaise", "Onion")),
            buildMeal(2, "Mashed Potatoes", ingredients = listOf("Potato", "Butter", "Milk")),
            buildMeal(3, "French Fries", ingredients = listOf("Potato", "Oil", "Salt")),
            buildMeal(4, "Tomato Soup", ingredients = listOf("Tomato", "Water", "Salt"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val inputCount = -3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
        verify(exactly = 0) { mealRepository.getAllMeals() }
    }

    @Test
    fun `getRandomPotatoMeals should handle duplicate potato meals`() {

        // Given
        val meals = listOfNotNull(
            buildMeal(1, "Potato Salad", ingredients = listOf("Potato", "Mayonnaise", "Onion")),
            buildMeal(2, "Mashed Potatoes", ingredients = listOf("Potato", "Butter", "Milk")),
            buildMeal(3, "French Fries", ingredients = listOf("Potato", "Oil", "Salt")),
            buildMeal(4, "Tomato Soup", ingredients = listOf("Tomato", "Water", "Salt"))
        )
        every { mealRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getRandomPotatoMeals.getRandomPotatoMeals(3)

        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(3)
        verify(exactly = 1) { mealRepository.getAllMeals() }
    }

 }