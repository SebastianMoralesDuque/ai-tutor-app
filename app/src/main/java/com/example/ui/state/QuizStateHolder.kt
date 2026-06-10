package com.example.ui.state

import android.util.Log
import com.example.data.models.Mistake
import com.example.data.models.SessionProgress
import com.example.data.repository.TutorRepository
import com.example.domain.model.Lesson
import com.example.ui.AnswerResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds quiz-related UI state and provides actions for selecting answers and submitting.
 *
 * Plain class (not a ViewModel) — accepts its dependencies via constructor.
 * Designed to be instantiated by TutorViewModel and have its StateFlows delegated.
 */
class QuizStateHolder(
    private val repository: TutorRepository,
    private val scope: CoroutineScope,
    private val onApiSubmitAnswer: suspend (questionId: String, answer: String) -> Unit
) {
    private val _selectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

    private val _isSubmitted = MutableStateFlow(false)
    val isSubmitted: StateFlow<Boolean> = _isSubmitted.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _answerResults = MutableStateFlow<Map<String, AnswerResult>>(emptyMap())
    val answerResults: StateFlow<Map<String, AnswerResult>> = _answerResults.asStateFlow()

    private val _isQuizSubmitting = MutableStateFlow(false)
    val isQuizSubmitting: StateFlow<Boolean> = _isQuizSubmitting.asStateFlow()

    fun selectQuizAnswer(questionId: Int, optionIndex: Int) {
        if (!_isSubmitted.value) {
            val updated = _selectedAnswers.value.toMutableMap()
            updated[questionId] = optionIndex
            _selectedAnswers.value = updated
        }
    }

    fun submitQuizAnswers(lesson: Lesson) {
        if (_isSubmitted.value || _isQuizSubmitting.value) return

        scope.launch {
            _isQuizSubmitting.value = true
            try {
                var correctCount = 0
                val currentSelections = _selectedAnswers.value
                val results = mutableMapOf<String, AnswerResult>()

                // Evaluate locally first (instant feedback)
                lesson.quizQuestions.forEach { question ->
                    val selectedIdx = currentSelections[question.id]
                    val chosenText = if (selectedIdx != null) {
                        question.options.getOrNull(selectedIdx) ?: ""
                    } else {
                        ""
                    }

                    val isCorrect = selectedIdx == question.correctAnswerIndex
                    val correctAnswerText = question.options.getOrNull(question.correctAnswerIndex) ?: ""

                    results[question.questionId] = AnswerResult(
                        correct = isCorrect,
                        feedback = "",
                        concept = ""
                    )
                    if (isCorrect) {
                        correctCount++
                    } else {
                        repository.saveMistake(
                            Mistake(
                                topic = lesson.topic,
                                question = question.question,
                                correctAnswer = correctAnswerText,
                                userAnswer = chosenText,
                                explanation = ""
                            )
                        )
                    }
                }

                _answerResults.value = results
                _score.value = correctCount
                _isSubmitted.value = true

                // Record completed lesson locally
                repository.saveSessionProgress(
                    SessionProgress(
                        topic = lesson.topic,
                        dayNumber = lesson.dayNumber,
                        score = correctCount
                    )
                )

                // Submit to API in background (fire and forget, doesn't block UI)
                scope.launch {
                    lesson.quizQuestions.forEach { question ->
                        val selectedIdx = currentSelections[question.id]
                        val chosenText = if (selectedIdx != null) {
                            question.options.getOrNull(selectedIdx) ?: ""
                        } else {
                            ""
                        }
                        if (question.questionId.isNotBlank() && chosenText.isNotBlank()) {
                            try {
                                onApiSubmitAnswer(question.questionId, chosenText)
                            } catch (e: Exception) {
                                Log.e("QuizStateHolder", "submitAnswer failed", e)
                            }
                        }
                    }
                }
            } finally {
                _isQuizSubmitting.value = false
            }
        }
    }

    /** Clears all quiz state for the next lesson load. */
    fun reset() {
        _selectedAnswers.value = emptyMap()
        _isSubmitted.value = false
        _score.value = 0
        _answerResults.value = emptyMap()
        _isQuizSubmitting.value = false
    }
}
