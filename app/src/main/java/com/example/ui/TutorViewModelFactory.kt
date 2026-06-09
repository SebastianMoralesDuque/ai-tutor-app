package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.repository.TutorRepository
import com.example.domain.usecases.GetChatTutorReplyUseCase
import com.example.domain.usecases.GetLessonUseCase
import com.example.domain.usecases.GetStreakUseCase

class TutorViewModelFactory(
    private val repository: TutorRepository,
    private val getLessonUseCase: GetLessonUseCase,
    private val getStreakUseCase: GetStreakUseCase,
    private val getChatTutorReplyUseCase: GetChatTutorReplyUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TutorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TutorViewModel(repository, getLessonUseCase, getStreakUseCase, getChatTutorReplyUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
