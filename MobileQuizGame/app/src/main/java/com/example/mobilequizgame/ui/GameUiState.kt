package com.example.mobilequizgame.ui

import com.example.mobilequizgame.data.Question

data class GameUiState(
    val currentQuestion: Question = Question("", listOf("", "", "", ""), ""),
    val currentQuestionCount: Int = 1,
    val score: Int = 0,
    val isGameOver: Boolean = false
)