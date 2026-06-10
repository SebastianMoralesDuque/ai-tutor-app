package com.example.data.repository.mock

import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion

class ProgrammingLessonGenerator : LessonGenerator {

    override fun matchesTopic(topic: String): Boolean {
        val normalized = topic.lowercase()
        return normalized.contains("python") || normalized.contains("programming") || normalized.contains("code")
    }

    override suspend fun generate(topic: String, dayNumber: Int): Lesson {
        return generateProgrammingLesson(dayNumber)
    }

    private fun generateProgrammingLesson(day: Int): Lesson {
        val adjustedDay = ((day - 1) % 3) + 1
        return when (adjustedDay) {
            1 -> Lesson(
                dayNumber = day,
                topic = "Python Programming",
                conceptTitle = "Dynamic Typing and Variables",
                shortExplanation = "Python is a dynamically-typed programming language. This means you do not need to declare variable types explicitly before assigning data. The interpreter infers and configures the variable type at runtime.",
                bulletPoints = listOf(
                    "Variables are created the moment they receive a value.",
                    "Types are evaluated dynamically when running the execution threads.",
                    "You can reassign variables to different data types with zero compile-time issues.",
                    "Functions like type() help inspect current data classes safely."
                ),
                simpleExample = "x = 42\nx = \"Now I am a string\"\nprint(x)",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 10,
                        question = "What does 'dynamically-typed' mean in Python?",
                        options = listOf(
                            "You must specify types manually for safety",
                            "Variable types are inferred and validated at runtime",
                            "The language compiles strictly to binary code",
                            "Variables once created cannot change values"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Dynamic typing allows programmers to omit written type variables, assigning integer, string, or list data flexibly as the application executes."
                    ),
                    QuizQuestion(
                        id = 11,
                        question = "How is a variable created in Python?",
                        options = listOf(
                            "Using a 'var' or 'let' keyword",
                            "By simply assigning a value using '='",
                            "Using the 'new' constructor keyword",
                            "Declaring its type like 'int x'"
                        ),
                        correctAnswerIndex = 1,
                        explanation = "Python variables need no formal keyword; they spring into existence upon their first evaluation mapping."
                    ),
                    QuizQuestion(
                        id = 12,
                        question = "What function helps us inspect the type of a variable?",
                        options = listOf("inspect()", "class()", "type()", "print()"),
                        correctAnswerIndex = 2,
                        explanation = "The type() built-in returns the exact active data-class metadata of any object passed to it."
                    )
                )
            )
            2 -> Lesson(
                dayNumber = day,
                topic = "Python Programming",
                conceptTitle = "Lists and Iterations",
                shortExplanation = "Lists are ordered, mutable collection blocks in Python used to package multiple items under a single identifier. Python structures clean 'for' loops that iterate directly through elements rather than manual indices.",
                bulletPoints = listOf(
                    "Lists are defined using brackets: my_list = [1, 2, 3].",
                    "List elements are index-accessed starting from 0.",
                    "Lists can hold mixed elements, including nested structures.",
                    "Use 'for item in list' syntax to loop cleanly without integer markers."
                ),
                simpleExample = "fruits = [\"apple\", \"berry\"]\nfor fruit in fruits:\n    print(fruit)",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 13,
                        question = "Which bracket symbol is used to declare lists in Python?",
                        options = listOf("Square brackets []", "Curly braces {}", "Parentheses ()", "Angled brackets <>"),
                        correctAnswerIndex = 0,
                        explanation = "Square brackets [] define mutable sequence arrays, keeping standard elements ordered."
                    ),
                    QuizQuestion(
                        id = 14,
                        question = "What is the index of the first item in a Python list?",
                        options = listOf("-1", "0", "1", "It is randomized"),
                        correctAnswerIndex = 1,
                        explanation = "Standard computer programming conventions index collection arrays starting from zero (0)."
                    ),
                    QuizQuestion(
                        id = 15,
                        question = "How do you append an item to the end of list 'my_list'?",
                        options = listOf("my_list.add(item)", "my_list.push(item)", "my_list.append(item)", "my_list += item"),
                        correctAnswerIndex = 2,
                        explanation = "The .append() function is the standard built-in list method to add items to the back."
                    )
                )
            )
            else -> Lesson(
                dayNumber = day,
                topic = "Python Programming",
                conceptTitle = "Dictionary Mapping",
                shortExplanation = "Dictionaries are unordered, key-value mappings in Python, providing extremely high search efficiency. Keys must be unique, and values are accessed with high speed via hash structures.",
                bulletPoints = listOf(
                    "Dictionaries use curly braces: dict = {'name': 'Jean'}.",
                    "Access values using keys: dict['name'].",
                    "Keys must be immutable (strings, integers, tuples).",
                    "Methods like keys() and values() help inspect structures."
                ),
                simpleExample = "info = {\"topic\": \"Python\", \"level\": 1}\nprint(info[\"topic\"])",
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 16,
                        question = "What is the primary structure of Python dictionaries?",
                        options = listOf("Sequential elements", "Key-value mappings", "Immutable floating vectors", "LIFO stack structures"),
                        correctAnswerIndex = 1,
                        explanation = "Dictionaries map highly searchable, arbitrary keys (like names or IDs) directly to custom values."
                    ),
                    QuizQuestion(
                        id = 17,
                        question = "Which data type is INVALID as a dictionary key?",
                        options = listOf("String", "Integer", "List", "Tuple"),
                        correctAnswerIndex = 2,
                        explanation = "Lists are mutable containers, which means they are not hashable and cannot be used as dictionary keys."
                    ),
                    QuizQuestion(
                        id = 18,
                        question = "What is returned if we invoke .get('missing_key', 'default')?",
                        options = listOf("It throws an KeyException error", "'default' safe fallback string", "None object", "0 integer"),
                        correctAnswerIndex = 1,
                        explanation = "The `.get()` method prevents program crashes by returning a fallback default rather than throwing an error for missing elements."
                    )
                )
            )
        }
    }
}
