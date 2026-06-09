package com.example.data.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.data.models.UserProfile
import com.example.data.models.SessionProgress
import com.example.data.models.Mistake
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Query("DELETE FROM user_profiles")
    suspend fun clearProfile()
}

@Dao
interface SessionProgressDao {
    @Query("SELECT * FROM session_progress ORDER BY completedAt ASC")
    fun getAllProgress(): Flow<List<SessionProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: SessionProgress)

    @Query("DELETE FROM session_progress")
    suspend fun clearProgress()
}

@Dao
interface MistakeDao {
    @Query("SELECT * FROM mistakes ORDER BY timestamp DESC")
    fun getAllMistakes(): Flow<List<Mistake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: Mistake)

    @Query("DELETE FROM mistakes WHERE id = :id")
    suspend fun deleteMistake(id: Int)

    @Query("DELETE FROM mistakes")
    suspend fun clearAllMistakes()
}

@Database(
    entities = [UserProfile::class, SessionProgress::class, Mistake::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun sessionProgressDao(): SessionProgressDao
    abstract fun mistakeDao(): MistakeDao
}
