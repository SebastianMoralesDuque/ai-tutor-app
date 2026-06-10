package com.example.data.repository.mock

import com.example.domain.model.Lesson

/**
 * Strategy interface for generating mock lessons based on topic matching.
 *
 * Each implementation determines which topics it can handle via [matchesTopic].
 * The [LessonGeneratorRegistry] uses this to dispatch [generate] calls.
 */
interface LessonGenerator {

    /**
     * Returns true if this generator can produce lessons for the given [topic].
     */
    fun matchesTopic(topic: String): Boolean

    /**
     * Generates a mock [Lesson] for the given [topic] and [dayNumber].
     */
    suspend fun generate(topic: String, dayNumber: Int): Lesson
}
