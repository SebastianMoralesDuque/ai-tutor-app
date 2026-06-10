package com.example.data.repository

import android.content.SharedPreferences
import com.example.data.database.AppDatabase
import com.example.data.models.UserProfile
import com.example.data.models.SessionProgress
import com.example.data.models.Mistake
import kotlinx.coroutines.flow.Flow

class TutorRepository(
    private val database: AppDatabase,
    private val prefs: SharedPreferences
) {

    private val userProfileDao = database.userProfileDao()
    private val sessionProgressDao = database.sessionProgressDao()
    private val mistakeDao = database.mistakeDao()

    val userProfile: Flow<UserProfile?> = userProfileDao.getProfile()
    val allProgress: Flow<List<SessionProgress>> = sessionProgressDao.getAllProgress()
    val allMistakes: Flow<List<Mistake>> = mistakeDao.getAllMistakes()

    suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.insertProfile(profile)
    }

    suspend fun clearUserProfile() {
        userProfileDao.clearProfile()
    }

    suspend fun saveSessionProgress(progress: SessionProgress) {
        sessionProgressDao.insertProgress(progress)
    }

    suspend fun clearSessionProgress() {
        sessionProgressDao.clearProgress()
    }

    suspend fun saveMistake(mistake: Mistake) {
        mistakeDao.insertMistake(mistake)
    }

    suspend fun deleteMistake(id: Int) {
        mistakeDao.deleteMistake(id)
    }

    suspend fun clearAllMistakes() {
        mistakeDao.clearAllMistakes()
    }

    fun saveUserId(id: String) {
        prefs.edit().putString("user_id", id).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }
}
