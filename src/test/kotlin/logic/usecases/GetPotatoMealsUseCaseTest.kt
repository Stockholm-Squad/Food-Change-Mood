package logic.usecases

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
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1,"Potato Salad", listOf("Potato", "Mayonnaise", "Onion")),
                createMeal(2,"Mashed Potatoes", listOf("Potato", "Butter", "Milk")),
            )
        )

        val inputCount = 10
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(2)
    }

    @Test
    fun `getRandomPotatoMeals should return failure when no meals contain potato`() {
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Avocado Toast", listOf("Bread", "Avocado", "Salt", "Pepper", "Lemon juice")),
                createMeal(2, "Chicken Curry", listOf("Chicken", "Onion", "Garlic", "Ginger", "Spices"))
            )
        )

        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()!!.message).isEqualTo(Constants.NO_MEALS_FOR_POTATO)
    }

    @Test
    fun `getRandomPotatoMeals should return failure when repository fails`() {
        every { mealRepository.getAllMeals() } returns Result.failure(Exception("Repository Error"))

        val inputCount = 2
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()!!.message).contains(Constants.UNEXPECTED_ERROR)
    }

    @Test
    fun `getRandomPotatoMeals should handle case ignore potato filter`() {
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "POTATO FRIES", listOf("POTATO")),
                createMeal(2, "Mashed", listOf("potato")),
                createMeal(3, "Mixed", listOf("PoTaTo"))
            )
        )

        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(3)
    }

    @Test
    fun `getRandomPotatoMeals should return empty list when count is zero`() {
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Potato", listOf("oil", "potato", "salt")),
                createMeal(2,"Mashed Potatoes", listOf("Potato", "Butter", "Milk")),
                createMeal(3,"Rice and Chicken", listOf("Rice", "Chicken", "Spices")),
                createMeal(4,"Potato Salad", listOf("Potato", "Mayonnaise", "Onion")),
                createMeal(5,"Tomato Soup", listOf("Tomato", "Water", "Salt")),
            )
        )

        val inputCount = 0
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `getRandomPotatoMeals should skip meals with null ingredients`() {
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Potato Chips", listOf("Potato", "Oil")),
                createMeal(2, "Unknown Meal", null),
                createMeal(3, "Potato Stew", listOf("Potato", "Carrot"))
            )
        )

        val inputCount = 3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isSuccess).isTrue()
        val meals = result.getOrNull()!!
        assertThat(meals.size).isEqualTo(2)
        assertThat(meals.none { it.ingredients == null }).isTrue()
    }

    @Test
    fun `getRandomPotatoMeals should return empty list when count is negative`() {
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(createMeal(1, "Potato", listOf("Potato")))
        )

        val inputCount = -3
        val result = getRandomPotatoMeals.getRandomPotatoMeals(inputCount)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `getRandomPotatoMeals should handle duplicate potato meals`() {
        val potatoMeal = createMeal(1, "Potato Soup", listOf("Potato"))
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(potatoMeal, potatoMeal, potatoMeal)
        )

        val result = getRandomPotatoMeals.getRandomPotatoMeals(3)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.size).isEqualTo(3)
    }

    @Test
    fun `getRandomPotatoMeals should ignore meals with potato in name but not in ingredients`() {
        every { mealRepository.getAllMeals() } returns Result.success(
            listOf(
                createMeal(1, "Fake Potato Dish", listOf("Carrot", "Oil")),
                createMeal(2, "Real Potato Dish", listOf("Potato", "Oil"))
            )
        )

        val result = getRandomPotatoMeals.getRandomPotatoMeals(2)

        assertThat(result.isSuccess).isTrue()
        val meals = result.getOrNull()!!
        assertThat(meals.size).isEqualTo(1)
        assertThat(meals.all { meal -> meal.ingredients!!.any { it.equals("potato", ignoreCase = true) } }).isTrue()
    }

 }