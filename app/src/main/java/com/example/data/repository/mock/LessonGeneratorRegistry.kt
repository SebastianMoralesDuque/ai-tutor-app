package com.example.data.repository.mock

import com.example.domain.model.Lesson

/**
 * Registry that holds a prioritized list of [LessonGenerator] strategies.
 * The first generator whose [LessonGenerator.matchesTopic] returns true
 * is used to produce the lesson. A catch-all generator should be last.
 */
class LessonGeneratorRegistry(
    private val generators: List<LessonGenerator>
) {
    /**
     * Returns the first [LessonGenerator] that matches the given [topic].
     * Falls back to the last generator if no match is found (expected to be
     * [GenericLessonGenerator] or equivalent).
     */
    fun generatorFor(topic: String): LessonGenerator {
        return generators.firstOrNull { it.matchesTopic(topic) }
            ?: generators.last()
    }

    /**
     * Generates a mock [Lesson] by delegating to the matching generator.
     */
    suspend fun generate(topic: String, dayNumber: Int): Lesson {
        return generatorFor(topic).generate(topic, dayNumber)
    }
}
