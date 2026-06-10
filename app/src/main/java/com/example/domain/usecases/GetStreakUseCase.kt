package com.example.domain.usecases

import com.example.data.models.SessionProgress
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class GetStreakUseCase {

    fun execute(progressList: List<SessionProgress>): Int {
        if (progressList.isEmpty()) return 0

        // Map timestamps to unique LocalDate values (date only)
        val completedDays = progressList
            .map { dateFromEpochMillis(it.completedAt) }
            .distinct()
            .sortedDescending()

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val mostRecent = completedDays.first()

        // If the most recent study was neither today nor yesterday, the streak is 0
        if (mostRecent != today && mostRecent != yesterday) return 0

        var streakCount = 1
        var currentDay = mostRecent

        for (i in 1 until completedDays.size) {
            val expectedPreviousDay = currentDay.minusDays(1)
            val comparisonDay = completedDays[i]
            if (comparisonDay == expectedPreviousDay) {
                streakCount++
                currentDay = comparisonDay
            } else {
                break // Streak gap detected
            }
        }

        return streakCount
    }

    private fun dateFromEpochMillis(millis: Long): LocalDate =
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}
