package logic.usecases

import io.mockk.every
import io.mockk.mockk
import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetIngredientGameUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertFails

class GetIngredientGameUseCaseTest {
  private lateinit var mealRepository: MealsRepository
  private lateinit var ingredientGameUseCase: GetIngredientGameUseCase
  private lateinit var meal : Meal

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        ingredientGameUseCase = GetIngredientGameUseCase(mealRepository)
        meal = Meal(
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
    }

    @Test
    fun `startIngredientGame() should return correct Question when have meals`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))

        // When
        val result = ingredientGameUseCase.startIngredientGame()

        // Then
        assertEquals("Spaghetti", result.mealName)
        assert(result.options.contains(result.correctIngredient))
        assert(result.options.size == 3)
    }

    @Test
    fun `submitAnswer() should return true when user select correct answer`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))
        ingredientGameUseCase.startIngredientGame()
        // when
        val result = ingredientGameUseCase.submitAnswer("Tomato")
        // then
        assert(result)
    }

    @Test
    fun `submitAnswer() should return false when user select incorrect answer`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))
        ingredientGameUseCase.startIngredientGame()

        // when
        val result = ingredientGameUseCase.submitAnswer("NotFound")

        // then
        assert(!result)
    }

    @Test
    fun `startIngredientGame() should fails when repository has no meals`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.success(emptyList())

        // when & then
        assertFails { ingredientGameUseCase.startIngredientGame() }
    }

    @Test
    fun `startIngredientGame() should fails when repository failure`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.failure(Exception("Repository error"))

        // when & then
        assertFails { ingredientGameUseCase.startIngredientGame() }
    }

    @Test
    fun `startIngredientGame() should return question has a correct ingredient`() {
        // given
        val meal2 = Meal(
           name = "Pizza", ingredients = listOf("Cheese", "Tomato", "Oregano"), id = 12, contributorId = 5,
           minutes = null,
           submitted = null,
           tags = null,
           nutrition = null,
           numberOfSteps = null,
           steps = null,
           description = null,
           numberOfIngredients = null
        )
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal, meal2))

        // when
        val question = ingredientGameUseCase.startIngredientGame()

        // then
        assert(question.options.contains(question.correctIngredient))
    }
}