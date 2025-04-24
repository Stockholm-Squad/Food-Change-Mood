package data

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.example.data.reader.MealCsvReader
import org.example.data.utils.CsvLineHandler
import org.example.model.FoodChangeMoodExceptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.Test

class MealCsvReaderTest {
    private lateinit var csvFile: File
    private lateinit var csvLineHandler: CsvLineHandler

    @BeforeEach
    fun setUp() {
       csvFile = File.createTempFile("food", ".csv")
        csvLineHandler = mockk()
    }

    @AfterEach
    fun tearDown() {
        csvFile.delete()
    }

    @Test
    fun `readLinesFromFile () should read and process all valid lines except header`() {
        //Given
        csvFile.writeText(
            """
                name,calories
                Pizza,300
                Salad,150
            """.trimIndent()
        )

        every { csvLineHandler.handleLine("Pizza,300") } returns "Pizza:300"
        every {csvLineHandler.handleLine("Salad,150") } returns "Salad:150"

        val reader = MealCsvReader(csvFile, csvLineHandler)
        //When
        val result = reader.readLinesFromFile()

       //Then
       assertThat(result.getOrNull()).isEqualTo(listOf("Pizza:300", "Salad:150"))
    }

    @Test
    fun `readLinesFromFile () should skip invalid lines where handleLine returns null`() {
        //Given
        csvFile.writeText(
            """
                name,calories
                InvalidLine
                GoodLine,123
            """.trimIndent()
        )

        every { csvLineHandler.handleLine("InvalidLine") } returns null
        every { csvLineHandler.handleLine("GoodLine,123") } returns "Good:123"

        val reader = MealCsvReader(csvFile, csvLineHandler)
        //When
        val result = reader.readLinesFromFile()

       //Then
        assertThat(result.getOrNull()).isEqualTo(listOf("Good:123"))
    }
    @Test
    fun `readLinesFromFile () should return empty list if only header is present`() {
        // Given
        csvFile.writeText("name,calories\n")

        val reader = MealCsvReader(csvFile, csvLineHandler)

        // When
        val result = reader.readLinesFromFile()

        // Then
        val lines = result.getOrThrow()
        assertTrue(lines.isEmpty())
    }


    @Test
    fun `readLinesFromFile () should return failure if file does not exist`() {
        //Given
        val nonExistentFile = File("non_existent.csv")
        val reader = MealCsvReader(nonExistentFile, csvLineHandler)
       //When
        val result = reader.readLinesFromFile()
        //Then
        assertThrows<FoodChangeMoodExceptions.IOException.ReadFailedException> { result.getOrThrow() }
    }



}