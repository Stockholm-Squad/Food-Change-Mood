package data.dataSource

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.Meal
import org.example.data.dataSource.MealCsvDataSource
import org.example.data.dataSource.MealDataSource
import org.example.data.parser.MealParser
import org.example.data.reader.MealReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.listOfMeal
import utils.listOfRows

class MealCsvDataSourceTest {

    private lateinit var mealReader: MealReader
    private lateinit var mealParser: MealParser
    private lateinit var mealDataSource: MealDataSource

    @BeforeEach
    fun setUp() {
        mealReader = mockk(relaxed = true)
        mealParser = mockk(relaxed = true)
        mealDataSource = MealCsvDataSource(mealReader, mealParser)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealReader.readLinesFromFile() }
    }

    @Test
    fun `getAllMeals() should return success result with meals when reader returns success result and parser made the parsing successfully`() {
        //Given
        val meal1 = listOfMeal[0]
        val meal2 = listOfMeal[1]
        every { mealReader.readLinesFromFile() } returns Result.success(
            listOfRows
        )

        every { mealParser.parseLine(listOfRows[0]) } returns Result.success(meal1)
        every { mealParser.parseLine(listOfRows[1]) } returns Result.success(meal2)

        //When
        val result = mealDataSource.getAllMeals()

        //Then
        assertThat(result.getOrThrow()).isEqualTo(listOfMeal)
    }

    @Test
    fun `getAllMeals() should return failure result with exception when reader returns failure result with exception`() {
        //Given
        every { mealReader.readLinesFromFile() } returns Result.failure(Throwable())

        //When
        val result = mealDataSource.getAllMeals()

        //Then
        assertThrows<Throwable> { result.getOrThrow() }
    }

    @Test
    fun `getAllMeals() should return success result with empty list when reader returns success result and parser returns failure result with exception`() {
        //Given
        every { mealReader.readLinesFromFile() } returns Result.success(listOfRows)

        every { mealParser.parseLine(listOfRows[0]) } returns Result.failure(Throwable())
        every { mealParser.parseLine(listOfRows[1]) } returns Result.failure(Throwable())

        //When
        val result = mealDataSource.getAllMeals()

        //Then
        assertThat(result.getOrThrow()).isEqualTo(emptyList<Meal>())
    }
}