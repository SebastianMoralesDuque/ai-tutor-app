package com.example.ui.state

import com.example.api.ProgressResponse
import com.example.data.models.Mistake
import com.example.data.models.SessionProgress
import com.example.data.models.UserProfile
import com.example.data.repository.TutorRepository
import com.example.domain.LearningService
import com.example.domain.usecases.GetStreakUseCase
import com.example.ui.MAX_LESSONS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds progress-related state and computed flows derived from local data and API.
 *
 * Plain class (not a ViewModel) — accepts its dependencies via constructor.
 * Designed to be instantiated by TutorViewModel and have its StateFlows delegated.
 */
class ProgressStateHolder(
    private val repository: TutorRepository,
    private val getStreakUseCase: GetStreakUseCase,
    private val learningService: LearningService,
    private val scope: CoroutineScope
) {
    // Historical Progress Records
    val allProgress: StateFlow<List<SessionProgress>> = repository.allProgress
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Past mistakes list for Review
    val allMistakes: StateFlow<List<Mistake>> = repository.allMistakes
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current streak count based on completed sessions
    val streakCount: StateFlow<Int> = allProgress
        .map { progressList -> getStreakUseCase.execute(progressList) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // User profile from local database
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Day count based on current topic completions (capped at MAX_LESSONS)
    val currentDayForTopic: StateFlow<Int> = combine(userProfile, allProgress) { profile, progressList ->
        if (profile == null) {
            1
        } else {
            val activeTopicProgress = progressList.filter {
                it.topic.equals(profile.topic, ignoreCase = true)
            }
            minOf(activeTopicProgress.size + 1, MAX_LESSONS)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 1
    )

    private fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val date1 = java.time.Instant.ofEpochMilli(timestamp1)
            .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        val date2 = java.time.Instant.ofEpochMilli(timestamp2)
            .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        return date1 == date2
    }

    val isTodaySessionCompleted: StateFlow<Boolean> = combine(userProfile, allProgress) { profile, progressList ->
        if (profile == null) {
            false
        } else {
            val now = System.currentTimeMillis()
            progressList.any { progress ->
                progress.topic.equals(profile.topic, ignoreCase = true) && isSameDay(progress.completedAt, now)
            }
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Whether the user is locked from changing topic (has started but not finished all lessons)
    val isTopicLocked: StateFlow<Boolean> = combine(userProfile, allProgress) { profile, progressList ->
        if (profile == null) {
            false
        } else {
            val completedCount = progressList.count {
                it.topic.equals(profile.topic, ignoreCase = true)
            }
            completedCount > 0 && completedCount < MAX_LESSONS
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // API Progress (rich data from backend)
    private val _apiProgress = MutableStateFlow<ProgressResponse?>(null)
    val apiProgress: StateFlow<ProgressResponse?> = _apiProgress.asStateFlow()

    fun refreshProgressFromApi() {
        scope.launch {
            try {
                val progress = learningService.getProgress()
                _apiProgress.value = progress
            } catch (e: Exception) {
                android.util.Log.e("ProgressStateHolder", "getProgress failed", e)
            }
        }
    }

    fun clearHistory() {
        scope.launch {
            repository.clearSessionProgress()
            repository.clearAllMistakes()
        }
    }

    fun deleteMistake(id: Int) {
        scope.launch {
            repository.deleteMistake(id)
        }
    }
}
