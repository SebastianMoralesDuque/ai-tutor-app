package com.example.data.repository

import com.example.domain.LearningService
import com.example.domain.model.Lesson
import com.example.domain.model.QuizQuestion
import java.util.Locale

class MockLearningService : LearningService {

    override suspend fun getLesson(topic: String, dayNumber: Int): Lesson {
        val normalized = topic.lowercase(Locale.getDefault())
        return when {
            normalized.contains("android") || normalized.contains("kotlin") || normalized.contains("compose") -> {
                generateAndroidLesson(dayNumber)
            }
            normalized.contains("python") || normalized.contains("programming") || normalized.contains("code") -> {
                generateProgrammingLesson(dayNumber)
            }
            normalized.contains("french") || normalized.contains("spanish") || normalized.contains("german") || normalized.contains("language") -> {
                generateLanguageLesson(dayNumber)
            }
            normalized.contains("history") || normalized.contains("world") || normalized.contains("war") -> {
                generateHistoryLesson(dayNumber)
            }
            else -> {
                generateGenericLesson(topic, dayNumber)
            }
        }
    }

    override suspend fun getChatResponse(topic: String, userMessage: String): String {
        val normalizedMsg = userMessage.lowercase(Locale.getDefault())
        val formattedTopic = topic.ifBlank { "your selected topic" }

        return when {
            normalizedMsg.contains("hello") || normalizedMsg.contains("hi") || normalizedMsg.contains("hey") -> {
                "Hello! I am your AI learning companion. Ask me any conceptual question about **$formattedTopic**, and I'll break it down for you simple and clear."
            }
            normalizedMsg.contains("example") || normalizedMsg.contains("code") || normalizedMsg.contains("show me") || normalizedMsg.contains("practice") -> {
                "Great request! Here is a simple, practical illustration of a core concept in **$formattedTopic**:\n\n" +
                "```\n" +
                "// Illustrating best practice concept\n" +
                "fun demonstrateConcept() {\n" +
                "    println(\"Focusing on core building blocks of $formattedTopic\")\n" +
                "}\n" +
                "```\n\n" +
                "Key takeaway: Break massive goals down into self-contained modules. What specific part of this code would you like to tweak?"
            }
            normalizedMsg.contains("quiz") || normalizedMsg.contains("test") || normalizedMsg.contains("question") -> {
                "That's the spirit! To review, what would you say is the primary goal of studying **$formattedTopic**?\n\n" +
                "1) Scaling robust systems efficiently\n" +
                "2) Memorizing syntax blindly\n" +
                "3) Overcomplicating basic architecture\n\n" +
                "Reply with your guess!"
            }
            normalizedMsg.contains("why") || normalizedMsg.contains("how") -> {
                "That goes to the heart of **$formattedTopic**! It is designed this way to maintain high safety, separation of concerns, and predictability.\n\n" +
                "By decoupling logic from display and keeping memory load tight, the ecosystem solves performance leaks before they reach production. Does this model align with other things you have studied?"
            }
            else -> {
                "That's a very interesting point about **$formattedTopic**! In practical environments, developers and academics pay close attention to this area because it dictates scalability.\n\n" +
                "To explore this deeper, should we discuss the core lifecycle, look at a practical sample implementation, or jump into a quick review quiz?"
            }
        }
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
