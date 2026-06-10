package com.example.data.repository

import android.util.Log
import com.example.api.AiTutorApi
import com.example.api.ChatRequest
import com.example.api.CreateUserRequest
import com.example.api.DailySessionRequest
import com.example.api.ProgressResponse
import com.example.api.SubmitAnswerRequest
import com.example.api.SubmitAnswerResponse
import com.example.api.SuggestionsRequest
import com.example.api.UpdateUserRequest
import com.example.domain.LearningService
import com.example.domain.model.CycleInfo
import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion

class RemoteLearningService(
    private val api: AiTutorApi,
    private val userIdProvider: () -> String?
) : LearningService {

    override suspend fun getLesson(topic: String, dayNumber: Int): Lesson {
        val userId = userIdProvider()
            ?: throw Exception("Primero configurá tu perfil para iniciar una lección.")

        val response = api.getDailySession(DailySessionRequest(userId))
        if (!response.isSuccessful) {
            throw Exception("Error al cargar la lección (${response.code()}). Intentá de nuevo.")
        }
        val body = response.body() ?: throw Exception("El servidor devolvió una lección vacía.")

        return Lesson(
            dayNumber = dayNumber,
            topic = topic,
            conceptTitle = body.lesson.title,
            shortExplanation = body.lesson.explanation,
            bulletPoints = body.lesson.bullets,
            simpleExample = body.lesson.example,
            quizQuestions = body.quiz.mapIndexed { index, quizData ->
                QuizQuestion(
                    id = index + 1,
                    questionId = quizData.questionId,
                    question = quizData.question,
                    options = quizData.options,
                    correctAnswerIndex = quizData.correctAnswerIndex,
                    explanation = ""
                )
            },
            cycleInfo = CycleInfo(
                topic = body.cycleInfo.topic,
                concept = body.cycleInfo.concept,
                conceptIndex = body.cycleInfo.conceptIndex,
                dayInCycle = body.cycleInfo.dayInCycle,
                totalConcepts = body.cycleInfo.totalConcepts,
                topicCompleted = body.cycleInfo.topicCompleted
            )
        )
    }

    override suspend fun getChatResponse(topic: String, userMessage: String): String {
        val userId = userIdProvider()
            ?: return "Primero configurá tu perfil de aprendizaje para poder asistirte."

        return try {
            val response = api.chat(ChatRequest(userId, userMessage))
            if (response.isSuccessful) {
                response.body()?.response ?: "El tutor no respondió."
            } else {
                "Error del chat (${response.code()}). Intentá de nuevo."
            }
        } catch (e: Exception) {
            "No se pudo contactar al tutor: ${e.localizedMessage}"
        }
    }

    override suspend fun createUser(topic: String, dailyTime: Int): String {
        val response = api.createUser(CreateUserRequest(topic, dailyTime))
        if (!response.isSuccessful) {
            throw Exception("Error al crear usuario (${response.code()}).")
        }
        return response.body()?.id ?: throw Exception("ID de usuario vacío en la respuesta.")
    }

    override suspend fun updateUser(userId: String, currentTopic: String): Boolean {
        return try {
            val response = api.updateUser(userId, UpdateUserRequest(currentTopic))
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("RemoteLearningService", "updateUser failed", e)
            false
        }
    }

    override suspend fun submitAnswer(questionId: String, answer: String): SubmitAnswerResponse? {
        val userId = userIdProvider() ?: return null
        return try {
            val response = api.submitAnswer(SubmitAnswerRequest(userId, questionId, answer))
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("RemoteLearningService", "submitAnswer failed", e)
            null
        }
    }

    override suspend fun getSuggestions(): List<String> {
        val userId = userIdProvider() ?: return emptyList()
        return try {
            val response = api.getSuggestions(SuggestionsRequest(userId))
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            Log.e("RemoteLearningService", "getSuggestions failed", e)
            emptyList()
        }
    }

    override suspend fun getProgress(): ProgressResponse? {
        val userId = userIdProvider() ?: return null
        return try {
            val response = api.getProgress(userId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("RemoteLearningService", "getProgress failed", e)
            null
        }
    }
}
