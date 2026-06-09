package com.example.domain.usecases

import com.example.data.models.SessionProgress
import java.util.Calendar

class GetStreakUseCase {

    fun execute(progressList: List<SessionProgress>): Int {
        if (progressList.isEmpty()) return 0

        // Map timestamps to unique normalized Calendar dates (midnight-aligned)
        val completedDays = progressList.map { progress ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = progress.completedAt
            normalizeCalendar(cal)
        }.distinctBy { it.timeInMillis }.sortedByDescending { it.timeInMillis }

        if (completedDays.isEmpty()) return 0

        val today = Calendar.getInstance()
        normalizeCalendar(today)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        normalizeCalendar(yesterday)

        val mostRecent = completedDays.first()
        // If the most recent study was neither today nor yesterday, the streak is 0
        if (mostRecent.timeInMillis != today.timeInMillis && mostRecent.timeInMillis != yesterday.timeInMillis) {
            return 0
        }

        var streakCount = 1
        var currentDay = mostRecent

        for (i in 1 until completedDays.size) {
            val expectedPreviousDay = Calendar.getInstance().apply {
                timeInMillis = currentDay.timeInMillis
                add(Calendar.DAY_OF_YEAR, -1)
                normalizeCalendar(this)
            }

            val comparisonDay = completedDays[i]
            if (comparisonDay.timeInMillis == expectedPreviousDay.timeInMillis) {
                streakCount++
                currentDay = comparisonDay
            } else {
                break // Streak gap detected
            }
        }

        return streakCount
    }

    private fun normalizeCalendar(cal: Calendar): Calendar {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }
}
