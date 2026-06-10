package com.example.domain

import com.example.api.ProgressResponse
import com.example.api.SubmitAnswerResponse
import com.example.domain.model.Lesson

interface LearningService {
    suspend fun getLesson(topic: String, dayNumber: Int): Lesson
    suspend fun getChatResponse(topic: String, userMessage: String): String
    suspend fun createUser(topic: String, dailyTime: Int): String
    suspend fun updateUser(userId: String, currentTopic: String): Boolean
    suspend fun submitAnswer(questionId: String, answer: String): SubmitAnswerResponse?
    suspend fun getSuggestions(): List<String>
    suspend fun getProgress(): ProgressResponse?
}
