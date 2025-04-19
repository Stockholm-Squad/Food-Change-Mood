package presentation;

import org.example.logic.usecases.GetGuessGameUseCase

class GuessGameUI (
    private val getGuessGameUseCase: GetGuessGameUseCase
){
    fun playGuessGame(){
        getGuessGameUseCase.guessGame()



    }
}
