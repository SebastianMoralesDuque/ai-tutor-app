package com.example.domain.usecases

import com.example.domain.LearningService
import com.example.domain.model.Lesson

class GetLessonUseCase(internal val learningService: LearningService) {
    suspend fun execute(topic: String, dayNumber: Int): Lesson {
        val normalizedTopic = topic.ifBlank { "General Knowledge" }
        // Ensure day is at least 1
        val safeDay = if (dayNumber < 1) 1 else dayNumber
        return learningService.getLesson(normalizedTopic, safeDay)
    }
}
