package presentation.features


import com.google.common.collect.Multimaps.index
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Meal
import model.Nutrition
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.example.presentation.features.GetHealthyFastFoodMealsUI
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetHealthyFastFoodMealsUITest {
    private lateinit var getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
    private lateinit var getHealthyFastFoodMealsUI: GetHealthyFastFoodMealsUI
    private lateinit var outPutController:(String)->Unit

    @BeforeEach
    fun setUp(){
        getHealthyFastFoodUseCase= mockk(relaxed = true)
        outPutController=mockk(relaxed = true)
        getHealthyFastFoodMealsUI= GetHealthyFastFoodMealsUI(getHealthyFastFoodUseCase,outPutController)
    }

    @Test
    fun `showHealthyFastFoodMeals should print error message when use case fails`() {
        // Given
        val errorMessage = "Check Internet"
        every { getHealthyFastFoodUseCase.getHealthyFastFood() } returns Result.failure(Exception(errorMessage))

        val result= mutableListOf<String>()
        val getHealthyFastFoodMealsUI = GetHealthyFastFoodMealsUI(getHealthyFastFoodUseCase,{result.add(it)})


        // When
        getHealthyFastFoodMealsUI.showHealthyFastFoodMeals()

        // Then
        assertThat(result).contains("❌ Failed to load meals: $errorMessage")
    }
@Test
fun `showHealthyFastFoodMeals should print meal details when use case succeeds`() {
    // Given
    val meal = Meal(
        name = "Healthy Salad",
        id = 1,
        minutes = 10,
        contributorId = 123,
        submitted =null,
        tags = listOf("healthy", "quick"),
        nutrition = Nutrition(totalFat = 5.0f, protein = null, sodium = null,
            sugar = null, calories = null, saturatedFat = 1.0f, carbohydrates = 1.0f),
        numberOfSteps = 3,
        steps = listOf("Wash vegetables", "Chop vegetables", "Mix ingredients"),
        description = "A healthy and quick salad",
        ingredients = listOf("Lettuce", "Tomato", "Olive oil"),
        numberOfIngredients = 3
    )
    every { getHealthyFastFoodUseCase.getHealthyFastFood() } returns Result.success(listOf(meal))

    val result = mutableListOf<String>()
    val getHealthyFastFoodMealsUI = GetHealthyFastFoodMealsUI(getHealthyFastFoodUseCase,{result.add(it)})
   getHealthyFastFoodMealsUI

    // When
   getHealthyFastFoodMealsUI.showHealthyFastFoodMeals()

    // Then
    assertThat(result).contains(
        "Meal 1:\n" +
                "Name='${meal.name}'\n" +
                "ID=${meal.id}\n" +
                "Minutes=${meal.minutes}\n" +
                "ContributorID=${meal.contributorId}\n" +
                "Submitted='${meal.submitted}'\n" +
                "Tags=${meal.tags}\n" +
                "Nutrition=${meal.nutrition}\n" +
                "StepsCount=${meal.numberOfSteps}\n" +
                "Steps=${meal.steps}\n" +
                "Description='${meal.description?.take(30)}...'\n" +
                "Ingredients=${meal.ingredients}\n" +
                "IngredientsCount=${meal.numberOfIngredients}\n"
    )
}


}