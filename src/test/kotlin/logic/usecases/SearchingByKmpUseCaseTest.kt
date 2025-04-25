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
        "'5 minute bread pizza', 'piz'",
        "'5 minute bread pizza', 'MIN'",
        "'5 minute bread pizza', 'min'",
        "'pizza wrap', 'Pizza'",
        "'two 12 inch good and easy pepperoni pizzas', '12'",
        "'5 minute bread pizza', 'minute bread'",
        "'ww core polenta crust pizza', 'pizza'",
        "'v i pizza', 'i'",
        "'bread   butter pickle deviled eggs', 'butter pickle'",
        "'abcxabcdabcdabcy', 'abcxabcd'",
        "'abacababc', 'abac'",
        "'aaaaab', 'aaab'",
        "'abcabcabcabc', 'abcabcab'",
        "'abcdabcabcd', 'abcabcd'",
        "'abcabcabcd', 'abcabcd'",
        "'abcabcabcabcx', 'abcabcx'",
        "'abababcabababcabababc', 'abababc'",
        "'abcabcabcabcabcabc', 'abcabcabc'",
        "'aabaabaaa', 'aabaabaa'",
        "'  FooBarBaz  ', 'bar'",
        "'abababababab', 'abab'",
        "'abababababab', 'ababa'",
        "'abababababab', 'ababab'",
        "'abababababab', 'abababa'",
        "'abcabcabc', 'abcabcabc'",
        "'abc@def', '@'",
        "'abc@def', 'def'",
        "'abc@def', 'abc'",
        "'abcde', 'a'",
        "'abcde', 'b'",
        "'abcde', 'c'",
        "'abcde', 'd'",
        "'abcde', 'e'",
        "'abc', 'abc'",
        "'abcdef', 'abc'",
        "'abcdef', 'def'",
        "'123abc456', 'abc'",
        "'same', 'same'",
        "'aabaaabaaac', 'aabaaac'",
        "'aabaaabaaac', 'aabaaa'",
        "'aabaaabaaac', 'aabaaab'",
        "'aabaabaaa', 'aabaabaa'",
        "'aabaabaaa', 'aabaabaaa'",
        "'abcdef', 'abc'",
        "'abcdef', 'def'",
        "'123abc456', 'abc'",
        "'abcdef', 'abc'",
        "'abcdef', 'def'"
    )
    fun `searchByKmp() should return true for valid pattern matches`(
        nameMeal: String,
        pattern: String
    ) {
        val result = searchingByKmpUseCase.searchByKmp(nameMeal, pattern)
        assertThat(result).isTrue()
    }

    @ParameterizedTest
    @CsvSource(
        "null, 'piz'",
        "'bread pizza', null",
        "'bread pizza', ''",
        "null, ''",
        "'', 'piz'",
        "'pizza wrap', 'burg'",
        "'white spinach pizza oamc', 'b'",
        "'pizza wrap', 'wrap!'",
        "'pizza wrap', '@'",
        "'abcxabcdabcdabcy', 'abcdabca'",
        "'abacababc', 'abacabac'",
        "'xyzxyzxyzxyz', 'abc'",
        "'aaaaaaa', 'aaab'",
        "'  FooBarBaz  ', '  '",
        "'pizza wrap', '   '",
        "'5 minute bread pizza', 'minute pizza'",
        "'bread pizza', '   '",
        "'abcabcabc', 'abcabcabcd'",
        "'abc@def', 'ghi'",
        "'abc', 'abcd'",
        "'abc', 'abcdef'",
        "'abcde', 'f'",
        "'abc', ''",
        "'', 'abc'",
        "'', ''",
        "'abc def', '   '",
        "'abcdef', 'ghi'",
        "'aabaaabaaac', 'aabaaad'",
        "'aabaabaaa', 'aabaabaaaa'",
        "'   ', 'abc'",
        "'abacababaa', 'ababac'",
        "'abc', 'abcd'",
        "'abcdef', 'xyz'",
        "'abc', 'abcd'",
        "'abc', ''",
        "'abc def', '   '"
    )
    fun `searchByKmp() should return false for non-matching or invalid patterns`(
        nameMeal: String?,
        pattern: String?
    ) {
        val result = searchingByKmpUseCase.searchByKmp(nameMeal, pattern)
        assertThat(result).isFalse()
    }
}
