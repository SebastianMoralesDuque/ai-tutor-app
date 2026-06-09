package com.example.domain

import com.example.domain.model.Lesson

interface LearningService {
    suspend fun getLesson(topic: String, dayNumber: Int): Lesson
    suspend fun getChatResponse(topic: String, userMessage: String): String
}
