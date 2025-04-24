package logic.usecases.getseafoodmealsbyproteintest

import model.Meal
import model.Nutrition
import java.util.*

object SeaFoodMeals {

    private val firstMeal = Meal(
        name = "Pan fried fish",
        id = 1,
        minutes = 20,
        contributorId = 101,
        submitted = Date(),
        tags = listOf("seafood", "dinner"),
        nutrition = Nutrition(
            calories = 300f,
            totalFat = 10f,
            sugar = 1f,
            sodium = 200f,
            protein = 30f,
            saturatedFat = 2f,
            carbohydrates = 10f
        ),
        numberOfSteps = 3,
        steps = listOf("Prep", "Cook", "Serve"),
        description = "Delicious seafood dish packed with protein",
        ingredients = listOf("Fish", "Oil", "Garlic"),
        numberOfIngredients = 3
    )

    private val secondMeal = Meal(
        name = "Shrimp pasta",
        id = 2,
        minutes = 25,
        contributorId = 102,
        submitted = Date(),
        tags = listOf("seafood"),
        nutrition = Nutrition(
            calories = 400f,
            totalFat = 15f,
            sugar = 2f,
            sodium = 180f,
            protein = 20f,
            saturatedFat = 3f,
            carbohydrates = 30f
        ),
        numberOfSteps = 4,
        steps = listOf("Boil pasta", "Cook shrimp", "Mix", "Serve"),
        description = "A tasty seafood pasta",
        ingredients = listOf("Shrimp", "Pasta", "Cream"),
        numberOfIngredients = 3
    )

    private val thirdMeal = Meal(
        name = "Veggie salad",
        id = 3,
        minutes = 10,
        contributorId = 103,
        submitted = Date(),
        tags = listOf("vegetarian"),
        nutrition = Nutrition(
            calories = 200f,
            totalFat = 5f,
            sugar = 3f,
            sodium = 100f,
            protein = 5f,
            saturatedFat = 1f,
            carbohydrates = 20f
        ),
        numberOfSteps = 2,
        steps = listOf("Chop", "Mix"),
        description = "Fresh salad with no seafood",
        ingredients = listOf("Lettuce", "Tomato", "Cucumber"),
        numberOfIngredients = 3
    )
    private val mealWithSameLevel = Meal(
    name = "Vaggie salad",
    id = 3,
    minutes = 10,
    contributorId = 103,
    submitted = Date(),
    tags = listOf("vegetarian"),
    nutrition = Nutrition(
    calories = 200f,
    totalFat = 5f,
    sugar = 3f,
    sodium = 100f,
    protein = 5f,
    saturatedFat = 1f,
    carbohydrates = 20f
    ),
    numberOfSteps = 2,
    steps = listOf("Chop", "Mix"),
    description = "Fresh salad with no seafood",
    ingredients = listOf("Lettuce", "Tomato", "Cucumber"),
    numberOfIngredients = 3
    )
    private val mealWithNullProtein = Meal(
        name = "Shrimp pasta",
        id = 2,
        minutes = 25,
        contributorId = 102,
        submitted = Date(),
        tags = listOf("seafood"),
        nutrition = Nutrition(
            calories = 400f,
            totalFat = 15f,
            sugar = 2f,
            sodium = 180f,
            protein = null,
            saturatedFat = 3f,
            carbohydrates = 30f
        ),
        numberOfSteps = 4,
        steps = listOf("Boil pasta", "Cook shrimp", "Mix", "Serve"),
        description = "A tasty seafood pasta",
        ingredients = listOf("Shrimp", "Pasta", "Cream"),
        numberOfIngredients = 3
    )
    private val mealWithNoSeaFood = Meal(
        name = "pasta",
        id = 2,
        minutes = 25,
        contributorId = 102,
        submitted = Date(),
        tags = listOf("pasta"),
        nutrition = Nutrition(
            calories = 400f,
            totalFat = 15f,
            sugar = 2f,
            sodium = 180f,
            protein = 20f,
            saturatedFat = 3f,
            carbohydrates = 30f
        ),
        numberOfSteps = 4,
        steps = listOf("Boil pasta", "Cook shrimp", "Mix", "Serve"),
        description = "A tasty pasta",
        ingredients = listOf( "Pasta", "Cream"),
        numberOfIngredients = 3
    )
    private val allMeals = listOf(firstMeal, secondMeal, thirdMeal)
    private val allMealsContainsMealWithNullProtein = listOf(firstMeal, secondMeal, thirdMeal, mealWithNullProtein)
    private val allMealsWithMealsWithSameProteinLevel = listOf(firstMeal, secondMeal, thirdMeal, mealWithSameLevel)
    private val allMealsWithOneWithNoSeaFood = listOf(firstMeal, secondMeal, thirdMeal, mealWithNoSeaFood)
    fun getAllMeals() = allMeals
    fun getAllMealsContainsMealWithNullProtein() = allMealsContainsMealWithNullProtein
    fun getAllMealsWithMealsWithSameProteinLevel() = allMealsWithMealsWithSameProteinLevel
    fun getAllMealsWithOneWithNoSeaFood() = allMealsWithOneWithNoSeaFood

}