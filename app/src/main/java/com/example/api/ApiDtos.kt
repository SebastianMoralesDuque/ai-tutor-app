package com.example.api

import com.squareup.moshi.Json

// ─── Requests ──────────────────────────────────────────────────────────

data class CreateUserRequest(
    @Json(name = "topic") val topic: String,
    @Json(name = "daily_time") val dailyTime: Int
)

data class DailySessionRequest(
    @Json(name = "user_id") val userId: String
)

data class SubmitAnswerRequest(
    @Json(name = "user_id") val userId: String,
    @Json(name = "question_id") val questionId: String,
    @Json(name = "answer") val answer: String
)

data class ChatRequest(
    @Json(name = "user_id") val userId: String,
    @Json(name = "message") val message: String
)

data class SuggestionsRequest(
    @Json(name = "user_id") val userId: String
)

data class UpdateUserRequest(
    @Json(name = "current_topic") val currentTopic: String
)

// ─── Responses ─────────────────────────────────────────────────────────

data class UserResponse(
    @Json(name = "id") val id: String,
    @Json(name = "current_topic") val currentTopic: String,
    @Json(name = "daily_time") val dailyTime: Int,
    @Json(name = "current_concept_index") val currentConceptIndex: Int,
    @Json(name = "concept_day") val conceptDay: Int,
    @Json(name = "created_at") val createdAt: String
)

data class DailySessionResponse(
    @Json(name = "lesson") val lesson: LessonData,
    @Json(name = "quiz") val quiz: List<QuizData>,
    @Json(name = "cycle_info") val cycleInfo: CycleInfo
)

data class CycleInfo(
    @Json(name = "topic") val topic: String,
    @Json(name = "concept") val concept: String,
    @Json(name = "concept_index") val conceptIndex: Int,
    @Json(name = "day_in_cycle") val dayInCycle: Int,
    @Json(name = "total_concepts") val totalConcepts: Int,
    @Json(name = "topic_completed") val topicCompleted: Boolean
)

data class LessonData(
    @Json(name = "title") val title: String,
    @Json(name = "explanation") val explanation: String,
    @Json(name = "bullets") val bullets: List<String>,
    @Json(name = "example") val example: String
)

data class QuizData(
    @Json(name = "question_id") val questionId: String,
    @Json(name = "question") val question: String,
    @Json(name = "options") val options: List<String>,
    @Json(name = "correct_answer_index") val correctAnswerIndex: Int,
    @Json(name = "answer_type") val answerType: String
)

data class SubmitAnswerResponse(
    @Json(name = "correct") val correct: Boolean,
    @Json(name = "feedback") val feedback: String,
    @Json(name = "concept") val concept: String,
    @Json(name = "mastery_delta") val masteryDelta: Double,
    @Json(name = "updated_progress") val updatedProgress: ProgressData
)

data class ProgressData(
    @Json(name = "concept") val concept: String,
    @Json(name = "mastery_level") val masteryLevel: Double,
    @Json(name = "correct") val correct: Boolean
)

data class ProgressResponse(
    @Json(name = "user_id") val userId: String,
    @Json(name = "current_topic") val currentTopic: String,
    @Json(name = "streak") val streak: Int,
    @Json(name = "cycle") val cycle: CycleData,
    @Json(name = "topic_progress") val topicProgress: TopicProgressData,
    @Json(name = "concept_mastery") val conceptMastery: List<ConceptMastery>,
    @Json(name = "completed_topics") val completedTopics: List<CompletedTopic>,
    @Json(name = "recent_mistakes") val recentMistakes: List<MistakeData>
)

data class CycleData(
    @Json(name = "concept_index") val conceptIndex: Int,
    @Json(name = "day_in_cycle") val dayInCycle: Int,
    @Json(name = "total_concepts") val totalConcepts: Int,
    @Json(name = "days_per_concept") val daysPerConcept: Int
)

data class TopicProgressData(
    @Json(name = "topic") val topic: String,
    @Json(name = "status") val status: String,
    @Json(name = "concepts_completed") val conceptsCompleted: Int
)

data class CompletedTopic(
    @Json(name = "topic") val topic: String,
    @Json(name = "completed_at") val completedAt: String
)

data class ConceptMastery(
    @Json(name = "concept") val concept: String,
    @Json(name = "level") val level: Double,
    @Json(name = "sessions_done") val sessionsDone: Int,
    @Json(name = "completed") val completed: Boolean,
    @Json(name = "is_current") val isCurrent: Boolean,
    @Json(name = "last_reviewed") val lastReviewed: String
)

data class MistakeData(
    @Json(name = "concept") val concept: String,
    @Json(name = "error_description") val errorDescription: String,
    @Json(name = "question_text") val questionText: String,
    @Json(name = "user_answer") val userAnswer: String,
    @Json(name = "timestamp") val timestamp: String
)

data class ChatResponse(
    @Json(name = "response") val response: String
)

data class HealthResponse(
    @Json(name = "status") val status: String
)
