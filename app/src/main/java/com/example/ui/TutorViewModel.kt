package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.UserProfile
import com.example.data.repository.TutorRepository
import com.example.domain.model.ChatMessage
import com.example.domain.model.CycleInfo
import com.example.domain.model.Lesson
import com.example.domain.usecases.GetChatTutorReplyUseCase
import com.example.domain.usecases.GetLessonUseCase
import com.example.domain.usecases.GetStreakUseCase
import com.example.ui.state.ChatStateHolder
import com.example.ui.state.LessonStateHolder
import com.example.ui.state.ProgressStateHolder
import com.example.ui.state.QuizStateHolder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface LessonState {
    object Loading : LessonState
    data class Success(val lesson: Lesson) : LessonState
    data class Error(val message: String) : LessonState
}

const val MAX_LESSONS = 3

data class AnswerResult(
    val correct: Boolean,
    val feedback: String,
    val concept: String
)

class TutorViewModel(
    private val repository: TutorRepository,
    private val getLessonUseCase: GetLessonUseCase,
    private val getStreakUseCase: GetStreakUseCase,
    private val getChatTutorReplyUseCase: GetChatTutorReplyUseCase
) : ViewModel() {

    private val learningService = getLessonUseCase.learningService

    private val quizStateHolder by lazy { QuizStateHolder(repository, viewModelScope) { qid, ans -> learningService.submitAnswer(qid, ans) } }
    private val chatStateHolder by lazy { ChatStateHolder(getChatTutorReplyUseCase, viewModelScope) }
    private val lessonStateHolder by lazy { LessonStateHolder(getLessonUseCase, repository, viewModelScope) }
    private val progressStateHolder by lazy { ProgressStateHolder(repository, getStreakUseCase, learningService, viewModelScope) }

    // Delegated state
    val quizSelectedAnswers: StateFlow<Map<Int, Int>> = quizStateHolder.selectedAnswers
    val isQuizSubmitted: StateFlow<Boolean> = quizStateHolder.isSubmitted
    val quizScore: StateFlow<Int> = quizStateHolder.score
    val answerResults: StateFlow<Map<String, AnswerResult>> = quizStateHolder.answerResults
    val isQuizSubmitting: StateFlow<Boolean> = quizStateHolder.isQuizSubmitting
    val chatMessages: StateFlow<List<ChatMessage>> = chatStateHolder.chatMessages
    val isChatLoading: StateFlow<Boolean> = chatStateHolder.isChatLoading
    val lessonState: StateFlow<LessonState> = lessonStateHolder.lessonState
    val isTopicCompleted: StateFlow<Boolean> = lessonStateHolder.isTopicCompleted
    val currentCycleInfo: StateFlow<CycleInfo?> = lessonStateHolder.currentCycleInfo
    val topicSuggestions: StateFlow<List<String>> = lessonStateHolder.topicSuggestions
    val allProgress = progressStateHolder.allProgress
    val allMistakes = progressStateHolder.allMistakes
    val streakCount = progressStateHolder.streakCount
    val apiProgress = progressStateHolder.apiProgress
    val currentDayForTopic = progressStateHolder.currentDayForTopic
    val isTodaySessionCompleted = progressStateHolder.isTodaySessionCompleted
    val isTopicLocked = progressStateHolder.isTopicLocked

    // Profile State
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        // Automatically load lessons whenever active topic or progression changes
        viewModelScope.launch {
            combine(userProfile, currentDayForTopic) { profile, day ->
                Pair(profile, day)
            }.collect { (profile, day) ->
                if (profile != null) {
                    if (!isTodaySessionCompleted.value && !lessonStateHolder.hasLoadedLesson) {
                        lessonStateHolder.hasLoadedLesson = true
                        loadTodayLesson(profile.topic, day)
                    }
                }
            }
        }
    }

    fun loadTodayLesson(topic: String, day: Int) {
        viewModelScope.launch {
            quizStateHolder.reset()
            lessonStateHolder.loadTodayLesson(topic, day)
        }
    }

    fun retryLoadLesson() {
        val profile = userProfile.value ?: return
        val day = currentDayForTopic.value
        lessonStateHolder.retryLoadLesson(profile.topic, day)
    }

    fun fetchSuggestions() {
        lessonStateHolder.fetchSuggestions()
    }

    fun saveGoals(topic: String, studyMinutes: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val existingUserId = repository.getUserId()

            if (existingUserId != null) {
                // User exists — update topic on the backend via PATCH
                try {
                    val success = learningService.updateUser(existingUserId, topic)
                    if (!success) {
                        Log.w("TutorViewModel", "updateUser returned false")
                    }
                } catch (e: Exception) {
                    Log.e("TutorViewModel", "updateUser failed", e)
                    // Continue offline — the local profile will be updated
                }
            } else {
                // First time — create user on the backend
                try {
                    val userId = learningService.createUser(topic, studyMinutes)
                    repository.saveUserId(userId)
                } catch (e: Exception) {
                    Log.e("TutorViewModel", "createUser failed", e)
                    repository.saveUserId(UUID.randomUUID().toString())
                }
            }

            // Save user profile locally to trigger the lesson load observer
            repository.saveUserProfile(UserProfile(topic = topic, dailyStudyMinutes = studyMinutes))

            // Reset lesson state for the new topic
            lessonStateHolder.reset()

            // Initialize chat welcome
            val welcomeMessage = "¡Estudiemos **$topic**! Estoy listo para responder preguntas o ayudarte con conceptos específicos."
            chatStateHolder.setWelcomeMessage(welcomeMessage)

            onComplete()
        }
    }

    fun selectQuizAnswer(questionId: Int, optionIndex: Int) {
        quizStateHolder.selectQuizAnswer(questionId, optionIndex)
    }

    fun submitQuizAnswers(lesson: Lesson) {
        quizStateHolder.submitQuizAnswers(lesson)
    }

    fun sendChatMessage(text: String) {
        val activeTopic = userProfile.value?.topic ?: "General Knowledge"
        chatStateHolder.sendChatMessage(text, activeTopic)
    }

    fun deleteMistake(id: Int) {
        progressStateHolder.deleteMistake(id)
    }

    fun clearHistory() {
        viewModelScope.launch {
            progressStateHolder.clearHistory()
            lessonStateHolder.reset()
            userProfile.value?.let {
                loadTodayLesson(it.topic, 1)
            }
        }
    }

    fun refreshProgressFromApi() {
        progressStateHolder.refreshProgressFromApi()
    }
}
