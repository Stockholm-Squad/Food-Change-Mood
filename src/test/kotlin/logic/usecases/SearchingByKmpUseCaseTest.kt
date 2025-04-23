package logic.usecases

import junit.framework.TestCase.assertFalse
import org.example.logic.usecases.SearchingByKmpUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue


class SearchingByKmpUseCaseTest {
     // charcter
    // truth
    // paramtize test
    private lateinit var searchingByKmpUseCase: SearchingByKmpUseCase

    @BeforeEach
    fun setUp() {
        searchingByKmpUseCase = SearchingByKmpUseCase()
    }

    @Test
    fun `should return true when pattern is found in meal name`() {
        val meal = meal("5 minute bread  pizza")
        val pattern = "piz"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern upper case match`() {
        val meal = meal("5 minute bread  pizza")
        val pattern = "MIN"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern lower case match`() {
        val meal = meal("5 minute bread  pizza")
        val pattern = "min"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern with mixed (upper & lower) case match`() {
        val meal = meal("pizza wrap")
        val pattern = "Pizza"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }


    @Test
    fun `should return true for pattern with digits inside meal name`() {
        val meal = meal("two 12 inch good and easy pepperoni pizzas")
        val pattern = "12"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern with spaces in meal name`() {
        val meal = meal("5 minute bread  pizza")
        val pattern = "minute bread"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true when pattern is found in 'ww core polenta crust pizza'`() {
        val meal = meal("ww core polenta crust pizza")
        val pattern = "pizza"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern with a single character that matches part of the meal name`() {
        val meal = meal("v i pizza")
        val pattern = "i"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern with leading and trailing spaces`() {
        val meal = meal("pizza wrap")
        val pattern = "  wrap  "
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true for pattern that matches with leading spaces only`() {
        val meal = meal("two 12 inch good and easy pepperoni pizzas")
        val pattern = "  pepperoni"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return true when meal name has multiple spaces between words`() {
        val meal = meal("bread   butter pickle deviled eggs")
        val pattern = "butter pickle"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertTrue(result)
    }

    @Test
    fun `should return false when pattern contains only spaces`() {
        val meal = meal("pizza wrap")
        val pattern = "   "
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }


    @Test
    fun `should return false when pattern is not found in meal name`() {
        val meal = meal("5 minute bread  pizza")
        val pattern = "burg"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }

    @Test
    fun `should return false when pattern is empty`() {
        val meal = meal("5 minute bread  pizza")
        val pattern = ""
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }

    @Test
    fun `should return false when meal name is null`() {
        val meal = meal(null)
        val pattern = "piz"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }

    @Test
    fun `should return false when pattern partially matches meal name but not completely`() {
        val meal = meal("5 minute bread pizza")
        val pattern = "minute pizza"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }

    @Test
    fun `should return false for pattern with a very short string that does not match`() {
        val meal = meal("white spinach pizza oamc")
        val pattern = "b"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }

    @Test
    fun `should return false for pattern with non-matching punctuation`() {
        val meal = meal("pizza wrap")
        val pattern = "wrap!"
        val result = searchingByKmpUseCase.searchByKmp(meal.name, pattern)
        assertFalse(result)
    }

}
