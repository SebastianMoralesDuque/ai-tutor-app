package com.example.data.repository.mock

import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion

class HistoryLessonGenerator : LessonGenerator {

    override fun matchesTopic(topic: String): Boolean {
        val normalized = topic.lowercase()
        return normalized.contains("history") || normalized.contains("world") || normalized.contains("war")
    }

    override suspend fun generate(topic: String, dayNumber: Int): Lesson {
        return generateHistoryLesson(dayNumber)
    }

    private fun generateHistoryLesson(day: Int): Lesson {
        val adjustedDay = ((day - 1) % 3) + 1
        return when (adjustedDay) {
            1 -> Lesson(
                dayNumber = day,
                topic = "World History",
                conceptTitle = "Rise of the Printing Press",
                shortExplanation = "Prior to the mid-15th century, manuscripts were handwritten, making knowledge Rare and strictly guarded by elites. Johannes Gutenberg's introduction of movable type printing transformed the flow of global ideas.",
                bulletPoints = listOf(
                    "The printing press emerged in Mainz, Germany, around the year 1440.",
                    "Movable metal type allowed fast, modular letter arrangements for mass printing.",
                    "It drove literacy heights and supported major historical trends like the Reformation.",
                    "Book prices plummeted, converting reading from private privilege to civic norm."
                ),
                simpleExample = "Before Gutenberg: ~30,000 books in Europe.\n50 years after: over 20,000,000 books in circulation.",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 28,
                        question = "What revolutionary technology did Johannes Gutenberg refine in Europe?",
                        options = listOf("The steam carriage", "The magnetic compass", "Movable type printing press", "The mechanical loom"),
                        correctAnswerIndex = 2,
                        explanation = "Gutenberg's movable type printing press in Mainz transformed historical communication channels, democratizing education permanently."
                    ),
                    QuizQuestion(
                        id = 29,
                        question = "Which immediate historical movement was accelerated by the mass printing of text?",
                        options = listOf("Sumerian farming", "The Protestant Reformation", "The Roman Empire division", "Digital banking"),
                        correctAnswerIndex = 1,
                        explanation = "The Protestant Reformation used printed pamphlets to translate and critique traditional structures, spreading core ideas to millions quickly."
                    ),
                    QuizQuestion(
                        id = 30,
                        question = "About what decade did the Gutenberg movable print system debut?",
                        options = listOf("1440s", "1780s", "1060s", "1210s"),
                        correctAnswerIndex = 0,
                        explanation = "The system emerged around 1440 in Europe, setting the stage for Renaissance shifts."
                    )
                )
            )
            2 -> Lesson(
                dayNumber = day,
                topic = "World History",
                conceptTitle = "The Silk Road Exchange",
                shortExplanation = "The Silk Road was not a single highway, but a vast network of ancient trade routes connecting China and East Asia with central Asia, India, Persia, and the Mediterranean civilizations for thousands of years.",
                bulletPoints = listOf(
                    "Established during the Han Dynasty (130 BCE) and active until modern shipping.",
                    "Silk was China's premiere luxury export, fiercely guarded as a state secret.",
                    "It served as the core pipeline for global religions like Buddhism and Islam.",
                    "Crucially, it also spread devastating pandemics like the Black Death."
                ),
                simpleExample = "Caravans traveled thousands of miles across deserts, exchanging silk for Roman glassware, horses, and spices.",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 31,
                        question = "What was the primarily traded luxury commodity from China along these ancient routes?",
                        options = listOf("Potatoes", "Silk fabric", "Iron ore", "Glassware"),
                        correctAnswerIndex = 1,
                        explanation = "High-quality, highly valued silk was China's premium trade monopoly, popular with wealthy Western elites."
                    ),
                    QuizQuestion(
                        id = 32,
                        question = "Which dynasty formally opened up Chinese trade routes for western caravan travel?",
                        options = listOf("Han Dynasty", "Ming Dynasty", "Tang Dynasty", "Qing Dynasty"),
                        correctAnswerIndex = 0,
                        explanation = "The Han Dynasty formally integrated trade corridors under Emperor Wu, around 130 BCE."
                    ),
                    QuizQuestion(
                        id = 33,
                        question = "What negative cargo of the Silk Road devastated European populations in the 14th century?",
                        options = listOf("Cacao beans", "Gunpowder leaks", "The Black Death plague", "Synthetic dyes"),
                        correctAnswerIndex = 2,
                        explanation = "The trade routes directly carried the bubonic plague (Black Death) vectors, clearing major urban populations."
                    )
                )
            )
            else -> Lesson(
                dayNumber = day,
                topic = "World History",
                conceptTitle = "Space Race Achievements",
                shortExplanation = "The mid-20th century Space Race was a direct geopolitical rivalry between the US and the USSR. Starting as military missile defense, it transformed into a competition for technological dominance beyond Earth.",
                bulletPoints = listOf(
                    "Launched in 1957 with the Soviet deployment of Sputnik 1 satellite.",
                    "It pushed computer science and communications tech at blinding speeds.",
                    "America's Apollo 11 moon landing in 1969 established a historic benchmark.",
                    "It eventually yielded peaceful collaborations like the International Space Station."
                ),
                simpleExample = "USSR: First satellite (Sputnik), first human (Yuri Gagarin).\nUS: First manned lunar landing (Neil Armstrong).",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 34,
                        question = "What Soviet achievement kicked off the Space Race in 1957?",
                        options = listOf("Launching Sputnik 1", "Landing on Mars", "Deploying the Hubble telescope", "Building Space Shuttle Discovery"),
                        correctAnswerIndex = 0,
                        explanation = "Sputnik 1, the first artificial satellite, shocked the global space community and sparked massive scientific research sprints."
                    ),
                    QuizQuestion(
                        id = 35,
                        question = "Who was the first human to orbit in outer space?",
                        options = listOf("Neil Armstrong", "Buzz Aldrin", "Yuri Gagarin", "John Glenn"),
                        correctAnswerIndex = 2,
                        explanation = "Yuri Gagarin became the first cosmic voyager in 1961, piloting the Soviet Vostok 1 capsule."
                    ),
                    QuizQuestion(
                        id = 36,
                        question = "What landing was achieved in July 1969?",
                        options = listOf("Landing on Mars", "Manned moon landing", "Probing Jupiter", "First space walk"),
                        correctAnswerIndex = 1,
                        explanation = "US Apollo 11 landed astronauts on the Moon, meeting President Kennedy's monumental decadal challenge."
                    )
                )
            )
        }
    }
}
