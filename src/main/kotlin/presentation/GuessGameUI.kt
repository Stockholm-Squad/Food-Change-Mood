package presentation;

import org.example.logic.GetGuessGameUseCase

class GuessGameUI (
    private val getGuessGameUseCase: GetGuessGameUseCase
){
    fun playGuessGame(){
        getGuessGameUseCase.GuessGame()



    }
}
