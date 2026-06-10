package com.example.domain.usecases

import com.example.domain.LearningService

class GetChatTutorReplyUseCase(internal val learningService: LearningService) {
    suspend fun execute(topic: String, userMessage: String): String {
        return learningService.getChatResponse(topic, userMessage)
    }
}
