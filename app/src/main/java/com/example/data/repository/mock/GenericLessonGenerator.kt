package com.example.data.repository.mock

import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion
import java.util.Locale

class GenericLessonGenerator : LessonGenerator {

    override fun matchesTopic(topic: String): Boolean = true

    override suspend fun generate(topic: String, dayNumber: Int): Lesson {
        return generateGenericLesson(topic, dayNumber)
    }

    private fun generateGenericLesson(topic: String, day: Int): Lesson {
        val capitalizedTopic = topic.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val adjustedDay = ((day - 1) % 3) + 1
        return when (adjustedDay) {
            1 -> Lesson(
                dayNumber = day,
                topic = capitalizedTopic,
                conceptTitle = "Foundational Principles of $capitalizedTopic",
                shortExplanation = "Every discipline relies on key core pillars that govern how practical elements interact. Understanding the fundamental components of $capitalizedTopic allows us to see how variables are measured, composed, and applied.",
                bulletPoints = listOf(
                    "Fundamental blocks must be clearly categorized and bounded.",
                    "Input actions dictate consistent transformations within the $capitalizedTopic systems.",
                    "Core configurations must prioritize safety, structural consistency, and clear documentation.",
                    "Consistent practice of primitive concepts prevents major downstream errors."
                ),
                simpleExample = "Focusing on simple actions, standard outputs, and isolated variables helps build robust mental models of $capitalizedTopic.",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 100 + day,
                        question = "What is the most effective way to start learning $capitalizedTopic?",
                        options = listOf(
                            "Attempting complex layouts immediately",
                            "Mastering foundational elements and modular basics first",
                            "Ignoring standard guidelines",
                            "Relying solely on external automated tools"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Focusing on core building blocks prevents scaling failures and structural errors, establishing strong conceptual confidence."
                    ),
                    QuizQuestion(
                        id = 101 + day,
                        question = "Why is structured documentation important in $capitalizedTopic?",
                        options = listOf(
                            "It makes variables run physically faster",
                            "It simplifies collaboration and keeps system states predictable",
                            "It is legally required in all programming languages",
                            "It prevents computer screen glares"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Documentation builds collective clarity, allowing different teams to scale the application components predictably."
                    ),
                    QuizQuestion(
                        id = 102 + day,
                        question = "What must we do to verify conceptual understanding?",
                        options = listOf("Test assumptions in daily sessions", "Study complex projects with no basic training", "Guess values random-style", "Disable local databases"),
                        correctAnswerIndex = 0,
                        explanation = "Reviewing concepts consistently in short localized study sessions translates theoretical understanding to muscle memory."
                    )
                )
            )
            2 -> Lesson(
                dayNumber = day,
                topic = capitalizedTopic,
                conceptTitle = "Workflow and Component Lifecycle in $capitalizedTopic",
                shortExplanation = "$capitalizedTopic is dynamic—it does not exist in a vacuum. It progresses through a definite lifecycle, moving from initialization, through steady execution transformations, and concluding with teardown or release.",
                bulletPoints = listOf(
                    "Initialization establishes the state environment cleanly.",
                    "Mutations occur only when specific validated stimulus events are triggered.",
                    "Re-evaluating states recursively prevents memory resource leaks.",
                    "Cleanup routines are critical to release locked assets safely."
                ),
                simpleExample = "Understanding the dynamic lifecycles of $capitalizedTopic helps isolate bugs before states accumulate errors.",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 103 + day,
                        question = "What marks the first stage of any system component in $capitalizedTopic?",
                        options = listOf("Teardown", "Recompilation", "Initialization", "State mutation"),
                        correctAnswerIndex = 2,
                        explanation = "Initialization prepares properties and registers listeners, ensuring the environment is safe for subsequent business actions."
                    ),
                    QuizQuestion(
                        id = 104 + day,
                        question = "Why are cleanup routines critical in advanced resource lifecycles?",
                        options = listOf(
                            "To clear up disk space physically",
                            "To prevent resource leaks and avoid freezing system threads",
                            "To make the compiled APK smaller",
                            "To change visual themes"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Releasing memory handles, network threads, and database transactions prevents crashes of client apps."
                    ),
                    QuizQuestion(
                        id = 105 + day,
                        question = "When should variables in $capitalizedTopic update their state values?",
                        options = listOf("Constantly in infinite running threads", "Only after positive validation events", "Never, all values must stay static", "At random time offsets"),
                        correctAnswerIndex = 1,
                        explanation = "Controlled state mutation based on validated trigger events ensures consistent predictability."
                    )
                )
            )
            else -> Lesson(
                dayNumber = day,
                topic = capitalizedTopic,
                conceptTitle = "Best Practices and Design Refinements of $capitalizedTopic",
                shortExplanation = "Moving from entry competency to master developer involves adopting architectural patterns that separate concerns. By isolating functional logic from raw visual presentation, we keep $capitalizedTopic flexible and extendable.",
                bulletPoints = listOf(
                    "Always enforce separation of concerns: decouple display layers.",
                    "Structure functions to do one logical workflow with minimal variables.",
                    "Design systems defensively, anticipating invalid states gracefully.",
                    "Commit to steady iterative updates rather than singular monolithic changes."
                ),
                simpleExample = "Dividing $capitalizedTopic actions into dedicated, testable sub-modules represents professional production craftsmanship.",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 106 + day,
                        question = "What pattern enforces modular system safety?",
                        options = listOf("Monolithic single loops", "Separation of concerns", "Hardcoding strings everywhere", "Skipping error validation"),
                        correctAnswerIndex = 1,
                        explanation = "Separating visual components, database access, and business states guarantees that bug fixes in one area won't damage other systems."
                    ),
                    QuizQuestion(
                        id = 107 + day,
                        question = "What does it mean to think with defensive design parameters?",
                        options = listOf(
                            "Building visual layout panels only",
                            "Anticipating invalid states and protecting boundaries gracefully",
                            "Refusing to update dependencies",
                            "Overcomplicating primary variables"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Defensive design implements fallbacks for unexpected states, preventing fatal crashes during runtime failures."
                    ),
                    QuizQuestion(
                        id = 108 + day,
                        question = "How should updates be rolled out in $capitalizedTopic?",
                        options = listOf("In massive, non-tested packages", "Steady iterative additions", "Only after years of complete pauses", "Through direct binary hacks"),
                        correctAnswerIndex = 1,
                        explanation = "Regularly adding small, well-tested iterations speeds up developer feedback while maintaining stable production states."
                    )
                )
            )
        }
    }
}
