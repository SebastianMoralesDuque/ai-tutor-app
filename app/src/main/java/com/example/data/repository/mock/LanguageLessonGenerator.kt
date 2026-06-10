package com.example.data.repository.mock

import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion

class LanguageLessonGenerator : LessonGenerator {

    override fun matchesTopic(topic: String): Boolean {
        val normalized = topic.lowercase()
        return normalized.contains("french") || normalized.contains("spanish") || normalized.contains("german") || normalized.contains("language")
    }

    override suspend fun generate(topic: String, dayNumber: Int): Lesson {
        return generateLanguageLesson(dayNumber)
    }

    private fun generateLanguageLesson(day: Int): Lesson {
        val adjustedDay = ((day - 1) % 3) + 1
        return when (adjustedDay) {
            1 -> Lesson(
                dayNumber = day,
                topic = "Foreign Languages",
                conceptTitle = "Formal vs Informal Greetings",
                shortExplanation = "Many European and East Asian languages distinguish strictly between informal greetings used with friends and formal greetings used with elders, clients, or strangers to show social respect.",
                bulletPoints = listOf(
                    "In French, use 'Bonjour' for formal and 'Salut' for informal settings.",
                    "In Spanish, 'Hola' is adaptive but 'Buenos días' sets a polite professional standard.",
                    "Using correct pronouns defines the social context (e.g. Spanish 'Tú' vs 'Usted').",
                    "Greeting conventions often command local postures, gestures, or tone nuances."
                ),
                simpleExample = "Formal: Bonjour Monsieur, comment allez-vous?\nInformal: Salut, ça va?",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 19,
                        question = "In French greeting conventions, which word is used to address formal settings of respect?",
                        options = listOf("Salut", "Bonjour", "Heureux", "Ciao"),
                        correctAnswerIndex = 1,
                        explanation = "'Bonjour' is the polite, universal greeting to address teachers, customers, and strangers."
                    ),
                    QuizQuestion(
                        id = 20,
                        question = "Which pronoun represents the formal singular 'You' in Spanish?",
                        options = listOf("Tú", "Usted", "Vosotros", "Yo"),
                        correctAnswerIndex = 1,
                        explanation = "'Usted' is the respected formal register, conjugating verbs in third-person singular templates."
                    ),
                    QuizQuestion(
                        id = 21,
                        question = "Why do language models prioritize learning greeting registers first?",
                        options = listOf(
                            "To build complex scientific databases",
                            "To establish respectful and standard communication baselines",
                            "They are mathematically longer words",
                            "To bypass standard dictionary compilers"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Learning greeting registers helps users avoid unintended offense when adapting to local speaking communities."
                    )
                )
            )
            2 -> Lesson(
                dayNumber = day,
                topic = "Foreign Languages",
                conceptTitle = "Subject-Verb Conjugation Basics",
                shortExplanation = "Verbs modify their spellings and suffixes dramatically to agree with different subject pronouns. Mastering this pattern forms the absolute bedrock of grammar syntax structures in almost any spoken language.",
                bulletPoints = listOf(
                    "Every subject pronoun dictates a specific morphological verb suffix.",
                    "Regular verbs follow a predictable pattern (e.g. Spanish -ar, -er, -ir verbs).",
                    "Irregular verbs (like 'to be' and 'to have') require separate memorization profiles.",
                    "Incorrect conjugation can alter either who did the action or when it happened."
                ),
                simpleExample = "Spanish 'hablar' (to speak):\nYo hablo (I speak)\nNosotros hablamos (We speak)",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 22,
                        question = "What must a verb agree with in most grammar configurations?",
                        options = listOf("The subject pronoun", "The direct object length", "The local timezone", "Capitalization conventions"),
                        correctAnswerIndex = 0,
                        explanation = "Verbs must conjugate to agree with the performing subject pronoun (e.g. 'I speak' versus 'he speaks')."
                    ),
                    QuizQuestion(
                        id = 23,
                        question = "What characteristic defines an irregular verb?",
                        options = listOf(
                            "It only appears in questions",
                            "Its spelling is identical for all pronouns",
                            "It does not follow standard conjugation ending patterns",
                            "It has more than twenty syllables"
                        ),
                        correctAnswerIndex = 2,
                        explanation = "Irregular verbs violate standard rule tables (e.g. French 'être' conjugations are completely asymmetrical) and require direct memorization."
                    ),
                    QuizQuestion(
                        id = 24,
                        question = "Which is the correct French first-person singular of the verb 'avoir' (to have)?",
                        options = listOf("Je suis", "J'ai", "Tu as", "Nous avons"),
                        correctAnswerIndex = 1,
                        explanation = "J'ai is the present tense singular for 'I have' in French, combining the elided pronoun 'Je' with 'ai'."
                    )
                )
            )
            else -> Lesson(
                dayNumber = day,
                topic = "Foreign Languages",
                conceptTitle = "Gendered Nouns and Articles",
                shortExplanation = "Many languages organize nouns into grammatical classes or genders (usually masculine and feminine). The choice of accompanying articles and adjectives must agree with the noun's gender.",
                bulletPoints = listOf(
                    "A noun's grammatical gender does not always reflect biological gender.",
                    "Articles must match the gender: e.g., French 'un' (M) and 'une' (F).",
                    "Adjective suffixes modify their spelling to match noun genders.",
                    "Noun endings often give clues: e.g., Spanish words ending in '-o' are typically masculine, and '-a' are feminine."
                ),
                simpleExample = "French:\nLe livre (M) - The book\nLa table (F) - The table",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 25,
                        question = "True or False: Grammatical gender matches physical gender of objects 100% of the time.",
                        options = listOf("True", "False", "Only in historical books", "Only in legal documents"),
                        correctAnswerIndex = 1,
                        explanation = "Grammatical gender is an arbitrary word classification system. Elements like rocks, tables, and books are gendered with no physical relationship."
                    ),
                    QuizQuestion(
                        id = 26,
                        question = "Which Spanish article forms the default masculine singular 'the'?",
                        options = listOf("La", "El", "Un", "Los"),
                        correctAnswerIndex = 1,
                        explanation = "'El' is the definite masculine singular article, used for objects like 'el sol' (the sun)."
                    ),
                    QuizQuestion(
                        id = 27,
                        question = "In Spanish, words ending with the suffix '-a' typically express what category?",
                        options = listOf("Plural verbs", "Adverbs", "Feminine nouns", "Infinitive endings"),
                        correctAnswerIndex = 2,
                        explanation = "Spanish nouns ending in '-a' are typically feminine (like 'la manzana'), though exceptions exist."
                    )
                )
            )
        }
    }
}
