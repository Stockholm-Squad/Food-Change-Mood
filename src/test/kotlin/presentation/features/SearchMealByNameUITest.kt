package presentation.features

import io.mockk.mockk
import org.example.logic.usecases.GetMealByNameUseCase
import org.example.presentation.features.SearchMealByNameUI
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class SearchMealByNameUITest {
    private lateinit var getMealByNameUseCase: GetMealByNameUseCase
    private lateinit var searchMealByNameUI: SearchMealByNameUI

    @BeforeEach
    fun setUp() {

        getMealByNameUseCase = mockk()
        searchMealByNameUI = SearchMealByNameUI(getMealByNameUseCase)
    }

    @Test
    fun `should display meals when search is successful`() {
        val pattern = "pizza"
        val result = searchMealByNameUI.handleSearchByName()


    }
}