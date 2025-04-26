package presentation.features.utils

import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.presentation.features.utils.SearchUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchUtilsTest {

    private lateinit var searchUtils: SearchUtils
    private lateinit var reader: InputReader

    @BeforeEach
    fun setUp() {
        reader = mockk(relaxed = true)
        searchUtils = SearchUtils()
    }

    @Test
    fun `readNonBlankTrimmedInput returns trimmed input if not blank`() {
        // Given
        every { reader.readStringOrNull() } returns "   pizza   "

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assert(result == "pizza")
    }

    @Test
    fun `readNonBlankTrimmedInput returns null for blank input`() {
        // Given
        every { reader.readStringOrNull() } returns "   "

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assert(result == null)
    }

    @Test
    fun `isValidSearchAgainInput returns true for 'y'`() {
        // Given
        val input = "y"

        // When
        val result = searchUtils.isValidSearchAgainInput(input)

        // Then
        assert(result)
    }

    @Test
    fun `isValidSearchAgainInput returns false for invalid input`() {
        // Given
        val input = "maybe"

        // When
        val result = searchUtils.isValidSearchAgainInput(input)

        // Then
        assert(!result)
    }

    @Test
    fun `isNumericAndInRange returns true for valid input`() {
        // Given
        val input = "2"
        val size = 3

        // When
        val result = searchUtils.isNumericAndInRange(input, size)

        // Then
        assert(result)
    }

    @Test
    fun `isNumericAndInRange returns false for out-of-range input`() {
        // Given
        val input = "5"
        val size = 3

        // When
        val result = searchUtils.isNumericAndInRange(input, size)

        // Then
        assert(!result)
    }
}