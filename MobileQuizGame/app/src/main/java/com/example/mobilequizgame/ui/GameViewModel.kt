package com.example.mobilequizgame.ui

import androidx.lifecycle.ViewModel
import com.example.mobilequizgame.data.Question
import com.example.mobilequizgame.data.MAX_NO_OF_QUESTIONS
import com.example.mobilequizgame.data.SCORE_INCREASE
import com.example.mobilequizgame.data.allQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())

    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var usedQuestions: MutableSet<Question> = mutableSetOf()
    private lateinit var currentQuestion: Question

    init {
        resetGame()
    }

    fun resetGame() {
        usedQuestions.clear()
        _uiState.value = GameUiState(currentQuestion = pickRandomQuestionAndShuffle())
    }

    fun checkUserAnswer(userAnswer: String) {
        if (userAnswer.equals(currentQuestion.correctAnswer)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            updateGameState(_uiState.value.score)
        }
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedQuestions.size == MAX_NO_OF_QUESTIONS){
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            _uiState.update { currentState ->
                currentState.copy(
                    currentQuestion = pickRandomQuestionAndShuffle(),
                    currentQuestionCount = currentState.currentQuestionCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    private fun shuffleCurrentQuestion(question: Question): Question {
        val shuffleChoice = question.choice.toMutableList()
        shuffleChoice.shuffle()
        question.choice = shuffleChoice
        return question
    }

    private fun pickRandomQuestionAndShuffle(): Question {
        currentQuestion = allQuestions.random()
        if (usedQuestions.contains(currentQuestion)) {
            return pickRandomQuestionAndShuffle()
        } else {
            usedQuestions.add(currentQuestion)
            return shuffleCurrentQuestion(currentQuestion)
        }
    }
}
