package logic.usecases

import Fake.FakeMealRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetPotatoMealsUseCaseTest{

    private lateinit var mealRepository : MealsRepository
    private lateinit var getRandomPotatoMeals : GetPotatoMealsUseCase
    private lateinit var fakeMealRepository: FakeMealRepository

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
        val result = getRandomPotatoMeals.getRandomPotatoMeals(5)

        // Then
        assertThat(result.isSuccess).isTrue()
        val meals = result.getOrNull()
        assertThat(meals!!.size).isEqualTo(3)
        assertThat(meals.all { it.ingredients!!.any { ing -> ing.equals("potato", ignoreCase = true) } }).isTrue()
        }

 }