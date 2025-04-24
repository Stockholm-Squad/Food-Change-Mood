package presentation.features

import io.mockk.mockk
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProteinSeafoodRankingUITest(){
 private lateinit var getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase
  @BeforeEach
  fun setUp(){
   getSeaFoodByProteinRankUseCase = mockk(relaxed = true)
  }

}