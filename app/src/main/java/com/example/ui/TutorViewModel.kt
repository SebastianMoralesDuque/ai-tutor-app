package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.Mistake
import com.example.data.models.SessionProgress
import com.example.data.models.UserProfile
import com.example.data.repository.TutorRepository
import com.example.domain.model.ChatMessage
import com.example.domain.model.Lesson
import com.example.domain.usecases.GetChatTutorReplyUseCase
import com.example.domain.usecases.GetLessonUseCase
import com.example.domain.usecases.GetStreakUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface LessonState {
    object Loading : LessonState
    data class Success(val lesson: Lesson) : LessonState
    data class Error(val message: String) : LessonState
}

class TutorViewModel(
    private val repository: TutorRepository,
    private val getLessonUseCase: GetLessonUseCase,
    private val getStreakUseCase: GetStreakUseCase,
    private val getChatTutorReplyUseCase: GetChatTutorReplyUseCase
) : ViewModel() {

    // Profile State
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Historical Progress Records
    val allProgress: StateFlow<List<SessionProgress>> = repository.allProgress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Past mistakes list for Review
    val allMistakes: StateFlow<List<Mistake>> = repository.allMistakes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current streak count based on completed sessions
    val streakCount: StateFlow<Int> = allProgress
        .map { progressList -> getStreakUseCase.execute(progressList) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Day count based on current topic completions
    val currentDayForTopic: StateFlow<Int> = combine(userProfile, allProgress) { profile, progressList ->
        if (profile == null) {
            1
        } else {
            // Filter progress records that match the current active topic
            val activeTopicProgress = progressList.filter {
                it.topic.equals(profile.topic, ignoreCase = true)
            }
            activeTopicProgress.size + 1
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 1
    )

    // Reactive Lesson State
    private val _lessonState = MutableStateFlow<LessonState>(LessonState.Loading)
    val lessonState: StateFlow<LessonState> = _lessonState.asStateFlow()

    // Active Quiz Inputs: maps question ID to selected option index
    private val _quizSelectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val quizSelectedAnswers: StateFlow<Map<Int, Int>> = _quizSelectedAnswers.asStateFlow()

    // Represents if quiz has been submitted and user is reviewing
    private val _isQuizSubmitted = MutableStateFlow(false)
    val isQuizSubmitted: StateFlow<Boolean> = _isQuizSubmitted.asStateFlow()

    // Calculated quiz evaluation score (e.g. "2 out of 3")
    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    // Conversational Chat Messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    init {
        // Automatically load lessons whenever active topic or progression changes
        viewModelScope.launch {
            combine(userProfile, currentDayForTopic) { profile, day ->
                Pair(profile, day)
            }.collect { (profile, day) ->
                if (profile != null) {
                    loadTodayLesson(profile.topic, day)
                } else {
                    _lessonState.value = LessonState.Error("Please set your learning goal first.")
                }
            }
        }
    }

    fun loadTodayLesson(topic: String, day: Int) {
        viewModelScope.launch {
            _lessonState.value = LessonState.Loading
            _quizSelectedAnswers.value = emptyMap()
            _isQuizSubmitted.value = false
            _quizScore.value = 0
            try {
                val lesson = getLessonUseCase.execute(topic, day)
                _lessonState.value = LessonState.Success(lesson)
            } catch (e: Exception) {
                _lessonState.value = LessonState.Error(e.localizedMessage ?: "Unknown error loading lesson.")
            }
        }
    }

    fun saveGoals(topic: String, studyMinutes: Int) {
        viewModelScope.launch {
            repository.saveUserProfile(UserProfile(topic = topic, dailyStudyMinutes = studyMinutes))
            // Initialize chat welcome
            val welcomeMessage = "Let's study **$topic**! I'm ready to answer any questions or help explain specific concepts."
            _chatMessages.value = listOf(ChatMessage(welcomeMessage, false))
        }
    }

    fun selectQuizAnswer(questionId: Int, optionIndex: Int) {
        if (!_isQuizSubmitted.value) {
            val updated = _quizSelectedAnswers.value.toMutableMap()
            updated[questionId] = optionIndex
            _quizSelectedAnswers.value = updated
        }
    }

    fun submitQuizAnswers(lesson: Lesson) {
        if (_isQuizSubmitted.value) return

        viewModelScope.launch {
            var correctCount = 0
            val currentSelections = _quizSelectedAnswers.value

            lesson.quizQuestions.forEach { question ->
                val selectedIdx = currentSelections[question.id]
                if (selectedIdx == question.correctAnswerIndex) {
                    correctCount++
                } else {
                    // Record mistake
                    val chosenText = if (selectedIdx != null) {
                        question.options.getOrNull(selectedIdx) ?: "None"
                    } else {
                        "Unanswered"
                    }
                    val correctText = question.options.getOrNull(question.correctAnswerIndex) ?: "Unknown"

                    repository.saveMistake(
                        Mistake(
                            topic = lesson.topic,
                            question = question.question,
                            correctAnswer = correctText,
                            userAnswer = chosenText,
                            explanation = question.explanation
                        )
                    )
                }
            }

            _quizScore.value = correctCount
            _isQuizSubmitted.value = true

            // Record completed lesson
            repository.saveSessionProgress(
                SessionProgress(
                    topic = lesson.topic,
                    dayNumber = lesson.dayNumber,
                    score = correctCount
                )
            )
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank() || _isChatLoading.value) return

        val activeTopic = userProfile.value?.topic ?: "General Knowledge"
        val userMsg = ChatMessage(text, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isChatLoading.value = true
            try {
                val reply = getChatTutorReplyUseCase.execute(activeTopic, text)
                _chatMessages.value = _chatMessages.value + ChatMessage(reply, isUser = false)
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage("Sorry, I had trouble processing that request: ${e.localizedMessage}", isUser = false)
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun deleteMistake(id: Int) {
        viewModelScope.launch {
            repository.deleteMistake(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearSessionProgress()
            repository.clearAllMistakes()
            userProfile.value?.let {
                loadTodayLesson(it.topic, 1)
            }
        }
    }
}
