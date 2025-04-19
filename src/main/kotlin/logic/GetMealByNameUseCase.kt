package logic

import model.Meal
import org.example.logic.MealsRepository
import org.example.logic.SearchingByKmp


class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmp: SearchingByKmp
) {

    /**
     * Searches for meals whose name contains the given query (case-insensitive).
     *
     * @param query The name or part of the name to search for.
     * @return A list of meals matching the query.
     */
    fun getMealByName(query: String?): List<Meal> {
        if (query.isNullOrEmpty()) return emptyList()

        return mealsRepository.getAllMeals().filter { meal ->
            searchingByKmp.kmpSearch(meal.name, query)
        }
    }
}

