package com.example.data.repository

import com.example.api.ProgressResponse
import com.example.api.SubmitAnswerResponse
import com.example.data.repository.mock.LessonGeneratorRegistry
import com.example.data.repository.mock.MockChatResponder
import com.example.domain.LearningService
import com.example.domain.model.Lesson
import java.util.UUID

class MockLearningService(
    private val registry: LessonGeneratorRegistry,
    private val chatResponder: MockChatResponder
) : LearningService {

    override suspend fun getLesson(topic: String, dayNumber: Int): Lesson {
        return registry.generate(topic, dayNumber)
    }

    override suspend fun getChatResponse(topic: String, userMessage: String): String {
        return chatResponder.respond(topic, userMessage)
    }

    override suspend fun createUser(topic: String, dailyTime: Int): String {
        return UUID.randomUUID().toString()
    }

    override suspend fun submitAnswer(questionId: String, answer: String): SubmitAnswerResponse? {
        return null
    }

    override suspend fun updateUser(userId: String, currentTopic: String): Boolean {
        return true
    }

    override suspend fun getSuggestions(): List<String> {
        return listOf("Android & Kotlin", "Python Programming", "World History", "Foreign Languages")
    }

    override suspend fun getProgress(): ProgressResponse? {
        return null
    }
}
