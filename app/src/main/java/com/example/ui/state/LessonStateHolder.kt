package com.example.ui.state

import com.example.data.repository.TutorRepository
import com.example.domain.LearningService
import com.example.domain.model.CycleInfo
import com.example.domain.model.Lesson
import com.example.domain.usecases.GetLessonUseCase
import com.example.ui.LessonState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds lesson-related UI state and provides actions for loading lessons.
 *
 * Plain class (not a ViewModel) — accepts its dependencies via constructor.
 * Designed to be instantiated by TutorViewModel and have its StateFlows delegated.
 */
class LessonStateHolder(
    private val getLessonUseCase: GetLessonUseCase,
    private val repository: TutorRepository,
    private val scope: CoroutineScope
) {
    private val _lessonState = MutableStateFlow<LessonState>(LessonState.Loading)
    val lessonState: StateFlow<LessonState> = _lessonState.asStateFlow()

    private val _isTopicCompleted = MutableStateFlow(false)
    val isTopicCompleted: StateFlow<Boolean> = _isTopicCompleted.asStateFlow()

    private val _currentCycleInfo = MutableStateFlow<CycleInfo?>(null)
    val currentCycleInfo: StateFlow<CycleInfo?> = _currentCycleInfo.asStateFlow()

    private val _topicSuggestions = MutableStateFlow<List<String>>(emptyList())
    val topicSuggestions: StateFlow<List<String>> = _topicSuggestions.asStateFlow()

    private val learningService: LearningService = getLessonUseCase.learningService

    // Guards
    private var _suggestionsFetched = false
    var hasLoadedLesson = false

    fun loadTodayLesson(topic: String, day: Int) {
        scope.launch {
            _lessonState.value = LessonState.Loading
            try {
                val lesson = getLessonUseCase.execute(topic, day)
                lesson.cycleInfo?.let { cycle ->
                    _currentCycleInfo.value = cycle
                    _isTopicCompleted.value = cycle.topicCompleted
                }
                _lessonState.value = LessonState.Success(lesson)
            } catch (e: Exception) {
                hasLoadedLesson = false
                _lessonState.value = LessonState.Error(
                    e.localizedMessage ?: "Error desconocido al cargar la lección."
                )
            }
        }
    }

    fun retryLoadLesson(topic: String, day: Int) {
        hasLoadedLesson = false
        loadTodayLesson(topic, day)
    }

    fun fetchSuggestions() {
        if (_suggestionsFetched) return
        _suggestionsFetched = true
        scope.launch {
            _topicSuggestions.value = learningService.getSuggestions()
        }
    }

    fun reset() {
        _lessonState.value = LessonState.Loading
        _isTopicCompleted.value = false
        _currentCycleInfo.value = null
        hasLoadedLesson = false
    }
}
