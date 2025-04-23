package logic.usecases

import model.Meal
import model.Nutrition
import java.util.Date


fun meal(
    name: String? = "arriba   baked winter squash mexican style",
    id: Int = 137739,
    minutes: Int? = 55,
    contributorId: Int = 47892,
    submitted: Date? = Date(),
    tags: List<String>? = listOf("test", "food"),
    nutrition: Nutrition? = Nutrition(200.0F, 10.0F, 5.0F, 30.0F, 2.0F, 0.0F, 4.0F),
    numberOfSteps: Int? = 3,
    steps: List<String>? = listOf("Step 1", "Step 2", "Step 3"),
    description: String? = "Sample description",
    ingredients: List<String>? = listOf("Cheese", "Tomato", "Dough"),
    numberOfIngredients: Int? = 7
): Meal {
    return Meal(
        name = name,
        id = id,
        minutes = minutes,
        contributorId = contributorId,
        submitted = submitted,
        tags = tags,
        nutrition = nutrition,
        numberOfSteps = numberOfSteps,
        steps = steps,
        description = description,
        ingredients = ingredients,
        numberOfIngredients = numberOfIngredients
    )
}

