package dev.forcetower.destiny.core.model.chat

data class ChatMessage(
    val nick: String,
    val features: List<String>,
    val timestamp: Long,
    val data: String
)