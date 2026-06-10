package com.example.ui.state

import com.example.domain.model.ChatMessage
import com.example.domain.usecases.GetChatTutorReplyUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds chat-related UI state and provides actions for sending messages.
 *
 * Plain class (not a ViewModel) — accepts its dependencies via constructor.
 * Designed to be instantiated by TutorViewModel and have its StateFlows delegated.
 */
class ChatStateHolder(
    private val getChatTutorReplyUseCase: GetChatTutorReplyUseCase,
    private val scope: CoroutineScope
) {
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun sendChatMessage(text: String, topic: String) {
        if (text.isBlank() || _isChatLoading.value) return

        val activeTopic = topic.ifBlank { "General Knowledge" }
        val userMsg = ChatMessage(text, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg

        scope.launch {
            _isChatLoading.value = true
            try {
                val reply = getChatTutorReplyUseCase.execute(activeTopic, text)
                _chatMessages.value = _chatMessages.value + ChatMessage(reply, isUser = false)
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    "Sorry, I had trouble processing that request: ${e.localizedMessage}",
                    isUser = false
                )
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    /** Replaces messages with a single welcome message from the tutor. */
    fun setWelcomeMessage(message: String) {
        _chatMessages.value = listOf(ChatMessage(message, false))
    }
}
