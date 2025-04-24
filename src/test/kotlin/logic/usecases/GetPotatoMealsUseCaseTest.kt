package logic.usecases

import Fake.createMeal
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
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
        every { mealRepository.getAllMeals() } returns Result.success( listOf(
            createMeal(1, "Potato", listOf("oil", "potato", "salt")),
            createMeal(2,"Mashed Potatoes", listOf("Potato", "Butter", "Milk")),
            createMeal(3,"Rice and Chicken", listOf("Rice", "Chicken", "Spices")),
            createMeal(4,"Potato Salad", listOf("Potato", "Mayonnaise", "Onion")),
            createMeal(5,"Tomato Soup", listOf("Tomato", "Water", "Salt")),
            )
        )

        // When
        val inputCount = 5
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        val meals = result.getOrNull()
        assertThat(meals!!.size).isEqualTo(3)
        assertThat(meals.all { it.ingredients!!.any { ingredient -> ingredient.equals("potato", ignoreCase = true) } }).isTrue()
        }

    @Test
    fun `getRandomPotatoMeals should return all available potato meals if count exceeds available`() {

        // Given
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1,"Potato Salad", listOf("Potato", "Mayonnaise", "Onion")),
                createMeal(2,"Mashed Potatoes", listOf("Potato", "Butter", "Milk")),
            )
        )

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
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Avocado Toast", listOf("Bread", "Avocado", "Salt", "Pepper", "Lemon juice")),
                createMeal(2, "Chicken Curry", listOf("Chicken", "Onion", "Garlic", "Ginger", "Spices"))
            )
        )

        // When
        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()!!.message).isEqualTo(Constants.NO_MEALS_FOR_POTATO)
    }

    @Test
    fun `getRandomPotatoMeals should return failure when repository fails`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.failure(Exception("Repository Error"))

        // When
        val inputCount = 2
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()!!.message).contains(Constants.UNEXPECTED_ERROR)
    }

    @Test
    fun `getRandomPotatoMeals should handle case ignore potato filter`() {

        //Given
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "POTATO FRIES", listOf("POTATO")),
                createMeal(2, "Mashed", listOf("potato")),
                createMeal(3, "Mixed", listOf("PoTaTo"))
            )
        )

        // When
        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(3)
    }

    @Test
    fun `getRandomPotatoMeals should return empty list when count is zero`() {

        // Given
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Potato", listOf("oil", "potato", "salt")),
                createMeal(2,"Mashed Potatoes", listOf("Potato", "Butter", "Milk")),
                createMeal(3,"Rice and Chicken", listOf("Rice", "Chicken", "Spices")),
                createMeal(4,"Potato Salad", listOf("Potato", "Mayonnaise", "Onion")),
                createMeal(5,"Tomato Soup", listOf("Tomato", "Water", "Salt")),
            )
        )

        // When
        val inputCount = 0
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `getRandomPotatoMeals should skip meals with null ingredients`() {

        // Given
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Potato Chips", listOf("Potato", "Oil")),
                createMeal(2, "Unknown Meal", null),
                createMeal(3, "Potato Stew", listOf("Potato", "Carrot"))
            )
        )

        // When
        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        val meals = result.getOrNull()!!
        assertThat(meals.size).isEqualTo(2)
        assertThat(meals.none { it.ingredients == null }).isTrue()
    }

    @Test
    fun `getRandomPotatoMeals should return empty list when count is negative`() {

        // Given
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(createMeal(1, "Potato", listOf("Potato")))
        )

        // When
        val inputCount = -3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `getRandomPotatoMeals should handle duplicate potato meals`() {

        // Given
        val potatoMeal = createMeal(1, "Potato Soup", listOf("Potato"))
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(potatoMeal, potatoMeal, potatoMeal)
        )

        // When
        val result = getRandomPotatoMeals.getRandomPotatoMeals(3)

        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(3)
    }

    @Test
    fun `getRandomPotatoMeals should ignore meals with potato in name but not in ingredients`() {

        // Given
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Fake Potato Dish", listOf("Carrot", "Oil")),
                createMeal(2, "Real Potato Dish", listOf("Potato", "Oil"))
            )
        )

        // When
        val result = getRandomPotatoMeals.getRandomPotatoMeals(2)


        // Then
        assertThat(result.isSuccess).isTrue()
        val meals = result.getOrNull()!!
        assertThat(meals.size).isEqualTo(1)
        assertThat(meals.all { meal -> meal.ingredients!!.any { it.equals("potato", ignoreCase = true) } }).isTrue()
    }

 }