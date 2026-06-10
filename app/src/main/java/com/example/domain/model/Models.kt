package com.example.domain.model

data class QuizQuestion(
    val id: Int,
    val questionId: String = "",
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class CycleInfo(
    val topic: String,
    val concept: String,
    val conceptIndex: Int,
    val dayInCycle: Int,
    val totalConcepts: Int,
    val topicCompleted: Boolean
)

data class Lesson(
    val dayNumber: Int,
    val topic: String,
    val conceptTitle: String,
    val shortExplanation: String,
    val bulletPoints: List<String>,
    val simpleExample: String,
    val quizQuestions: List<QuizQuestion>,
    val cycleInfo: CycleInfo? = null
)

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
