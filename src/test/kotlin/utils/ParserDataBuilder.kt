package utils

import model.Meal
import model.Nutrition
import org.example.utils.getDateFromString

val listOfRows = listOf(
    "\"mixecan meal,137739,55,47892,2005-09-16," +
            "\"['60-minutes-or-less', 'time-to-make', 'course', 'main-ingredient', 'cuisine', 'preparation', 'occasion', 'north-american', 'side-dishes', 'vegetables', 'mexican', 'easy', 'fall', 'holiday-event', 'vegetarian', 'winter', 'dietary', 'christmas', 'seasonal', 'squash']\"" +
            ",\"[51.5, 0.0, 13.0, 0.0, 2.0, 0.0, 4.0]\",11," +
            "\"['make a choice and proceed with recipe', 'depending on size of squash , cut into half or fourths', 'remove seeds', 'for spicy squash , drizzle olive oil or melted butter over each cut squash piece', 'season with mexican seasoning mix ii', 'for sweet squash , drizzle melted honey , butter , grated piloncillo over each cut squash piece', 'season with sweet mexican spice mix', 'bake at 350 degrees , again depending on size , for 40 minutes up to an hour , until a fork can easily pierce the skin', 'be careful not to burn the squash especially if you opt to use sugar or butter', 'if you feel more comfortable , cover the squash with aluminum foil the first half hour , give or take , of baking', 'if desired , season with salt']\"," +
            "\"autumn is my favorite time of year to cook! this recipe \n can be prepared either spicy or sweet, your choice!" + "two of my posted mexican-inspired seasoning mix recipes are offered as suggestions.\"," +
            "\"['winter squash', 'mexican seasoning', 'mixed spice', 'honey', 'butter', 'olive oil', 'salt']\"," +
            "7",
    "\"mixecan meal,137739,55,47892,2005-09-16," +
            "\"['60-minutes-or-less', 'time-to-make', 'course', 'main-ingredient', 'cuisine', 'preparation', 'occasion', 'north-american', 'side-dishes', 'vegetables', 'mexican', 'easy', 'fall', 'holiday-event', 'vegetarian', 'winter', 'dietary', 'christmas', 'seasonal', 'squash']\"" +
            ",\"[51.5, 0.0, 13.0, 0.0, 2.0, 0.0, 4.0]\",11," +
            "\"['make a choice and proceed with recipe', 'depending on size of squash , cut into half or fourths', 'remove seeds', 'for spicy squash , drizzle olive oil or melted butter over each cut squash piece', 'season with mexican seasoning mix ii', 'for sweet squash , drizzle melted honey , butter , grated piloncillo over each cut squash piece', 'season with sweet mexican spice mix', 'bake at 350 degrees , again depending on size , for 40 minutes up to an hour , until a fork can easily pierce the skin', 'be careful not to burn the squash especially if you opt to use sugar or butter', 'if you feel more comfortable , cover the squash with aluminum foil the first half hour , give or take , of baking', 'if desired , season with salt']\"," +
            "\"autumn is my favorite time of year to cook! this recipe \n can be prepared either spicy or sweet, your choice!" + "two of my posted mexican-inspired seasoning mix recipes are offered as suggestions.\"," +
            "\"['winter squash', 'mexican seasoning', 'mixed spice', 'honey', 'butter', 'olive oil', 'salt']\"," +
            "7"
)

val listOfMeal = listOf(
    Meal(
        name = "mixecan meal",
        id = 137739,
        minutes = 55,
        contributorId = 47892,
        submitted = getDateFromString("2005-09-16").getOrNull(),
        tags = listOf(
            "60-minutes-or-less",
            "time-to-make",
            "course",
            "main-ingredient",
            "cuisine",
            "preparation",
            "occasion",
            "north-american",
            "side-dishes",
            "vegetables",
            "mexican",
            "easy",
            "fall",
            "holiday-event",
            "vegetarian",
            "winter",
            "dietary",
            "christmas",
            "seasonal",
            "squash"
        ),
        nutrition = Nutrition(
            calories = 51.5F,
            totalFat = 0.0F,
            sugar = 13.0F,
            sodium = 0.0F,
            protein = 2.0F,
            saturatedFat = 0.0F,
            carbohydrates = 4.0F
        ),
        numberOfSteps = 11,
        steps = listOf(
            "make a choice and proceed with recipe",
            "depending on size of squash , cut into half or fourths",
            "remove seeds",
            "for spicy squash , drizzle olive oil or melted butter over each cut squash piece",
            "season with mexican seasoning mix ii",
            "for sweet squash , drizzle melted honey , butter , grated piloncillo over each cut squash piece",
            "season with sweet mexican spice mix",
            "bake at 350 degrees , again depending on size , for 40 minutes up to an hour , until a fork can easily pierce the skin",
            "be careful not to burn the squash especially if you opt to use sugar or butter",
            "if you feel more comfortable , cover the squash with aluminum foil the first half hour , give or take , of baking",
            "if desired , season with salt"
        ),
        description = "autumn is my favorite time of year to cook! this recipe \n can be prepared either spicy or sweet, your choice!" + "two of my posted mexican-inspired seasoning mix recipes are offered as suggestions.",
        ingredients = listOf(
            "winter squash",
            "mexican seasoning",
            "mixed spice",
            "honey",
            "butter",
            "olive oil",
            "salt"
        ),
        numberOfIngredients = 7
    ),
    Meal(
        name = "mixecan meal",
        id = 137739,
        minutes = 55,
        contributorId = 47892,
        submitted = getDateFromString("2005-09-16").getOrNull(),
        tags = listOf(
            "60-minutes-or-less",
            "time-to-make",
            "course",
            "main-ingredient",
            "cuisine",
            "preparation",
            "occasion",
            "north-american",
            "side-dishes",
            "vegetables",
            "mexican",
            "easy",
            "fall",
            "holiday-event",
            "vegetarian",
            "winter",
            "dietary",
            "christmas",
            "seasonal",
            "squash"
        ),
        nutrition = Nutrition(
            calories = 51.5F,
            totalFat = 0.0F,
            sugar = 13.0F,
            sodium = 0.0F,
            protein = 2.0F,
            saturatedFat = 0.0F,
            carbohydrates = 4.0F
        ),
        numberOfSteps = 11,
        steps = listOf(
            "make a choice and proceed with recipe",
            "depending on size of squash , cut into half or fourths",
            "remove seeds",
            "for spicy squash , drizzle olive oil or melted butter over each cut squash piece",
            "season with mexican seasoning mix ii",
            "for sweet squash , drizzle melted honey , butter , grated piloncillo over each cut squash piece",
            "season with sweet mexican spice mix",
            "bake at 350 degrees , again depending on size , for 40 minutes up to an hour , until a fork can easily pierce the skin",
            "be careful not to burn the squash especially if you opt to use sugar or butter",
            "if you feel more comfortable , cover the squash with aluminum foil the first half hour , give or take , of baking",
            "if desired , season with salt"
        ),
        description = "autumn is my favorite time of year to cook! this recipe \n can be prepared either spicy or sweet, your choice!" + "two of my posted mexican-inspired seasoning mix recipes are offered as suggestions.",
        ingredients = listOf(
            "winter squash",
            "mexican seasoning",
            "mixed spice",
            "honey",
            "butter",
            "olive oil",
            "salt"
        ),
        numberOfIngredients = 7
    )
)