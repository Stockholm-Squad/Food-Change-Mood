package logic.usecases

import io.mockk.every
import io.mockk.mockk
import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetIngredientGameUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import com.google.common.truth.Truth

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
    fun `startIngredientGame should return valid question with correct ingredient included in options`() {
        // Given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))

        // When
        val result = ingredientGameUseCase.startIngredientGame()

        // Then
        assertTrue(result.isSuccess, "Expected result to be successful")
        val question = result.getOrNull()
        requireNotNull(question)
        Truth.assertThat(question.mealName).isEqualTo("Spaghetti")
        Truth.assertThat(question.options.contains(question.correctIngredient))
        Truth.assertThat(question.options.size).isEqualTo(3)
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
        Truth.assertThat(result)
    }



    @Test
    fun `submitAnswer() should return false when user select incorrect answer`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.success(listOf(meal))
        ingredientGameUseCase.startIngredientGame()

        // when
        val result = ingredientGameUseCase.submitAnswer("NotFound")

        // then
        Truth.assertThat(!result)
    }

    @Test
    fun `startIngredientGame() should fails when repository has no meals`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.success(emptyList())
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        // then
        Truth.assertThat(result.isFailure)
    }

    @Test
    fun `startIngredientGame() should fails when repository failure`() {
        // given
        every { mealRepository.getAllMeals() } returns Result.failure(Exception("Repository error"))
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        //  then
        Truth.assertThat(result.isFailure)
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
        val result = ingredientGameUseCase.startIngredientGame()

        // then
        Truth.assertThat(result.isSuccess)
        val question = result.getOrNull()
        Truth.assertThat(question).isNotNull()
        Truth.assertThat(question?.options?.contains(question.correctIngredient))
    }

    @Test
    fun `startIngredientGame should fail when all meals have no ingredients`() {
        //given
        val badMeal = meal.copy(ingredients = null)
        every { mealRepository.getAllMeals() } returns Result.success(listOf(badMeal))
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        //then
        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `startIngredientGame should fail when all meals have empty names`() {
        // given
        val badMeal = meal.copy(name = "")
        every { mealRepository.getAllMeals() } returns Result.success(listOf(badMeal))
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        //then
        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `startIngredientGame should still succeed with less than 3 unique ingredients`() {
        // given
        val minimalMeal = meal.copy(ingredients = listOf("Tomato"))
        every { mealRepository.getAllMeals() } returns Result.success(listOf(minimalMeal))
        // when
        val result = ingredientGameUseCase.startIngredientGame()
        // then
        Truth.assertThat(result.isSuccess).isTrue()
        val question = result.getOrNull()
        Truth.assertThat(question).isNotNull()
        Truth.assertThat(question?.options?.size).isAtMost(3)
    }

    @Test
    fun `startIngredientGame should fail if generateOptions fails`() {
        // given
        every { mealRepository.getAllMeals() } returnsMany listOf(
            Result.success(listOf(meal)),
            Result.failure(Exception("Options generation failed"))
        )
        // when
        val result = ingredientGameUseCase.startIngredientGame()
        // then
        Truth.assertThat(result.isFailure).isTrue()
    }


    @Test
    fun `startIngredientGame should work even if some meals have null ingredients`() {
        //given
        val incompleteMeal = meal.copy(ingredients = null)
        val validMeal = meal.copy(name = "Valid", ingredients = listOf("Salt", "Pepper"))
        every { mealRepository.getAllMeals() } returns Result.success(listOf(incompleteMeal, validMeal))
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        //given
        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `startIngredientGame should fail when all meals are invalid after filtering`() {
        //given
        val meal3 = meal.copy(name = null)
        val meal4 = meal.copy(ingredients = emptyList())

        every { mealRepository.getAllMeals() } returns Result.success(listOf( meal3,meal4))
        //when
        val result = ingredientGameUseCase.startIngredientGame()
        //then
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(IllegalStateException::class.java)
        Truth.assertThat(result.exceptionOrNull()).hasMessageThat().contains("no meals")
    }







}