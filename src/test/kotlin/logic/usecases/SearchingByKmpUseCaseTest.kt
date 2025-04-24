package logic.usecases

import com.google.common.truth.Truth.assertThat
import org.example.logic.usecases.SearchingByKmpUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class SearchingByKmpUseCaseTest {

    private lateinit var searchingByKmpUseCase: SearchingByKmpUseCase

    @BeforeEach
    fun setUp() {
        searchingByKmpUseCase = SearchingByKmpUseCase()
    }

    @ParameterizedTest
    @CsvSource(
        "'5 minute bread pizza', 'piz', true",
        "'5 minute bread pizza', 'MIN', true",
        "'5 minute bread pizza', 'min', true",
        "'pizza wrap', 'Pizza', true",
        "'two 12 inch good and easy pepperoni pizzas', '12', true",
        "'5 minute bread pizza', 'minute bread', true",
        "'ww core polenta crust pizza', 'pizza', true",
        "'v i pizza', 'i', true",
        "'pizza wrap', '  wrap  ', true",
        "'two 12 inch good and easy pepperoni pizzas', '  pepperoni', true",
        "'bread   butter pickle deviled eggs', 'butter pickle', true",
    )
    fun `should return true when pattern is found in meal name`(
        nameMeal: String,
        pattern: String,
        expect: Boolean
    ) {
        // When
        val result = searchingByKmpUseCase.searchByKmp(nameMeal, pattern)

        // Then
        assertThat(result).isEqualTo(expect)
    }


    @ParameterizedTest
    @CsvSource(
        "'pizza wrap', '   ', false",
        "'5 minute bread pizza', 'burg', false",
        "'5 minute bread pizza', '', false",
        "'null', 'piz', false",
        "'5 minute bread pizza', 'minute pizza', false",
        "'white spinach pizza oamc', 'b', false",
        "'pizza wrap', 'wrap!', false",
        "'pizza wrap', '@', false"
    )
    fun `should return false for pattern is not found in meal name`(
        nameMeal: String,
        pattern: String,
        expected: Boolean
    ) {
        // when
        val result = searchingByKmpUseCase.searchByKmp(nameMeal, pattern)

        // Then
        assertThat(result).isEqualTo(expected)
    }

}
