package com.example.domain.model

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class Lesson(
    val dayNumber: Int,
    val topic: String,
    val conceptTitle: String,
    val shortExplanation: String,
    val bulletPoints: List<String>,
    val simpleExample: String,
    val quizQuestions: List<QuizQuestion>
)

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
