package com.example.data.repository.mock

import java.util.Locale

class MockChatResponder {

    suspend fun respond(topic: String, userMessage: String): String {
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
}
