package com.example.data.repository.mock

import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion

class AndroidLessonGenerator : LessonGenerator {

    override fun matchesTopic(topic: String): Boolean {
        val normalized = topic.lowercase()
        return normalized.contains("android") || normalized.contains("kotlin") || normalized.contains("compose")
    }

    override suspend fun generate(topic: String, dayNumber: Int): Lesson {
        return generateAndroidLesson(dayNumber)
    }

    private fun generateAndroidLesson(day: Int): Lesson {
        val adjustedDay = ((day - 1) % 3) + 1
        return when (adjustedDay) {
            1 -> Lesson(
                dayNumber = day,
                topic = "Android & Jetpack Compose",
                conceptTitle = "Declaring User Interface (UI)",
                shortExplanation = "Jetpack Compose is Android's modern toolkit for building native UI using a declarative approach. Unlike traditional imperative XML layouts where you manually traverse and mutate view trees, in Compose you describe what the UI should look like for a given state.",
                bulletPoints = listOf(
                    "UI is expressed as a pure function of state: UI = f(State).",
                    "Composable functions are marked with the '@Composable' annotation.",
                    "The Compose compiler automatically updates only components whose active states changed, a process known as recomposition.",
                    "Scaffold provides a standardized, Material Design 3 structure for screens."
                ),
                simpleExample = "@Composable\nfun Greeting(name: String) {\n    Text(text = \"Hello, \$name!\")\n}",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "What is the primary characteristic of declarative UI in Jetpack Compose?",
                        options = listOf(
                            "You manually find views by ID and update them",
                            "You describe what the UI looks like for a given state",
                            "It compiles exclusively to HTML and JavaScript",
                            "It requires XML layouts to compile"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "In Jetpack Compose, the UI is declarative, meaning you write composable functions that describe your UI's appearance for a given state, rather than manually updating elements."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "Which annotation is required for a function to build UI in Jetpack Compose?",
                        options = listOf("@UiController", "@Layout", "@Composable", "@Inject"),
                        correctAnswerIndex = 2,
                        explanation = "@Composable is the essential compiler directive that allows standard Kotlin functions to define visual components."
                    ),
                    QuizQuestion(
                        id = 3,
                        question = "What is the automatic updates of UI components in Compose called?",
                        options = listOf("Recomposition", "Restructuring", "Redrawing", "Compilation"),
                        correctAnswerIndex = 0,
                        explanation = "Recomposition is the process where Compose re-runs composable functions when their underlying state parameters change."
                    )
                )
            )
            2 -> Lesson(
                dayNumber = day,
                topic = "Android & Jetpack Compose",
                conceptTitle = "State in Jetpack Compose",
                shortExplanation = "State in an application determines what is shown to the user at any given moment. In Jetpack Compose, state is tracked using MutableState, which triggers automatic recomposition when its value updates.",
                bulletPoints = listOf(
                    "Use 'mutableStateOf(value)' to create reactive state wrappers.",
                    "Wrap state in 'remember { ... }' to preserve its value across recompositions.",
                    "State hoisting is the pattern of moving state up to make a composable stateless and highly testable.",
                    "Use rememberSaveable to persist state across Activity configuration changes (like screen rotation)."
                ),
                simpleExample = "val count = remember { mutableStateOf(0) }\nButton(onClick = { count.value++ }) {\n    Text(\"Count: \${count.value}\")\n}",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 4,
                        question = "Why do we wrap mutableStateOf inside remember?",
                        options = listOf(
                            "To make the code run faster",
                            "To prevent the value from resetting during recomposition",
                            "To compile the function on a separate JVM thread",
                            "To enable offline database support"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "'remember' acts as standard cache. Without it, the state is re-initialized to its initial value whenever the outer Composable recomposes."
                    ),
                    QuizQuestion(
                        id = 5,
                        question = "What is 'State Hoisting' in Jetpack Compose?",
                        options = listOf(
                            "A mechanism to transfer data to server-side APIs",
                            "Moving state upwards to a common parent to make a Composable stateless",
                            "Applying visual lifts and animations to cards",
                            "Creating databases using Room"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "State hoisting decouples a composable from how its state is stored, passing state value down and events up, making the rendering highly modular."
                    ),
                    QuizQuestion(
                        id = 6,
                        question = "Which state holder survives screen rotations automatically?",
                        options = listOf("remember", "rememberSaveable", "mutableStateOf", "stateOf"),
                        correctAnswerIndex = 1,
                        explanation = "rememberSaveable saves state inside Bundle objects, allowing values to persist through configuration events like rotary shifts."
                    )
                )
            )
            else -> Lesson(
                dayNumber = day,
                topic = "Android & Jetpack Compose",
                conceptTitle = "Standard Layout Composables",
                shortExplanation = "Compose layout coordinates let us align components. Since Composable functions represent self-contained design outputs, we position them sequentially using built-in arrangement containers: Row, Column, and Box.",
                bulletPoints = listOf(
                    "Column arranges items vertically down the screen.",
                    "Row arranges items horizontally left-to-right (respecting local RTL directions).",
                    "Box overlays items on top of each other, allowing stack layouts.",
                    "Use HorizontalArrangement and VerticalAlignment to fine-tune spacing."
                ),
                simpleExample = "Column {\n    Text(\"Top Item\")\n    Row {\n        Text(\"Left\")\n        Text(\"Right\")\n    }\n}",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 7,
                        question = "Which layout container stacked elements directly on top of each other?",
                        options = listOf("Row", "Column", "Box", "Grid"),
                        correctAnswerIndex = 2,
                        explanation = "Box acts like a stack container, layering child elements sequentially in 3D visual coordinates (z-axis)."
                    ),
                    QuizQuestion(
                        id = 8,
                        question = "How are children of a 'Column' arranged visually?",
                        options = listOf(
                            "Horizontally side-by-side",
                            "Vertically one after another",
                            "Stacked in the center",
                            "In a circular loop"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Columns align elements vertically downwards, which makes them perfect for traditional scrolling form structures."
                    ),
                    QuizQuestion(
                        id = 9,
                        question = "What parameter specifies space parameters between items in a Row?",
                        options = listOf("horizontalArrangement", "verticalAlignment", "modifier", "spacing"),
                        correctAnswerIndex = 0,
                        explanation = "The horizontalArrangement parameter is used to declare space distribution rules (such as SpaceBetween or SpaceAround) for children of a Row."
                    )
                )
            )
        }
    }
}
