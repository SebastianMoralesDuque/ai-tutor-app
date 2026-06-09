package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val topic: String,
    val dailyStudyMinutes: Int
)

@Entity(tableName = "session_progress")
data class SessionProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val dayNumber: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val score: Int
)

@Entity(tableName = "mistakes")
data class Mistake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val question: String,
    val correctAnswer: String,
    val userAnswer: String,
    val explanation: String,
    val timestamp: Long = System.currentTimeMillis()
)
