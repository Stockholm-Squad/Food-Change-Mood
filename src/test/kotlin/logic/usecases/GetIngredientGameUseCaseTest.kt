package logic.usecases

import io.mockk.every
import io.mockk.mockk
import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetIngredientGameUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import kotlin.test.Test


class GetIngredientGameUseCaseTest {
  private lateinit var mealRepository: MealsRepository
  private lateinit var ingredientGameUseCase: GetIngredientGameUseCase
  private lateinit var meal : Meal

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        ingredientGameUseCase = GetIngredientGameUseCase(mealRepository)
        meal = buildMeal(
            name = "Spaghetti",
            ingredients = listOf("Tomato", "Basil", "Garlic"),
            id = 12,
            contributorId = 5,
        )
    }

    @Test
    fun `startIngredientGame should return valid question with correct ingredient included in options`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))

        // When
        val result = ingredientGameUseCase.startIngredientGame()

        // Then
        assertTrue(result.isSuccess, "Expected result to be successful")
        val question = result.getOrNull()
        requireNotNull(question)
        assertEquals("Spaghetti", question.mealName)
        assertTrue(question.options.contains(question.correctIngredient))
        assertEquals(3, question.options.size, "There should be exactly 3 options")
    }


    @Test
    fun `submitAnswer should return true when user selects correct answer`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))
        val questionResult = ingredientGameUseCase.startIngredientGame()
        val question = questionResult.getOrNull()
        requireNotNull(question)

        // When
        val result = ingredientGameUseCase.submitAnswer(question.correctIngredient)

        // Then
        assertTrue(result)
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
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        // then
        assert(result.isFailure)
    }

    @Test
    fun `startIngredientGame() should fails when repository failure`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.failure(Exception("Repository error"))
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        //  then
        assert(result.isFailure)
    }

    @Test
    fun `startIngredientGame() should return question has a correct ingredient`() {
        // given
        val meal2 = buildMeal(
            name = "Pizza",
            ingredients = listOf("Cheese", "Tomato", "Oregano"),
            id = 12,
            contributorId = 5,
        )
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal, meal2))

        // when
        val result = ingredientGameUseCase.startIngredientGame()

        // then
        assertTrue(result.isSuccess, "Expected result to be successful")
        val question = result.getOrNull()
        requireNotNull(question)
        assert(question.options.contains(question.correctIngredient))
    }
}