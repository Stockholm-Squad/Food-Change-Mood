package data.parser

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.example.data.parser.MealCsvParser
import org.example.data.parser.MealParser
import org.example.data.utils.LineFormater
import org.example.utils.DateParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.listOfMeal
import utils.mealAsListOfStrings
import utils.mealAsString

class MealCsvParserTest {

    private lateinit var dateParser: DateParser
    private lateinit var lineFormater: LineFormater
    private lateinit var mealParser: MealParser

    @BeforeEach
    fun setUp() {
        dateParser = mockk(relaxed = true)
        lineFormater = mockk(relaxed = true)
        mealParser = MealCsvParser(lineFormater, dateParser)
    }

    @Test
    fun `parseLine() should return success result of meal when Row of meal is valid`() {
        //Given
        every { lineFormater.formatMealLine(mealAsString) } returns mealAsListOfStrings
        every { dateParser.getDateFromString("2005-09-16") } returns Result.success(LocalDate(2005, 9, 16))

        //When
        val result = mealParser.parseLine(mealAsString)

        //Then
        assertThat(result.getOrThrow()).isEqualTo(listOfMeal[0])
    }
}