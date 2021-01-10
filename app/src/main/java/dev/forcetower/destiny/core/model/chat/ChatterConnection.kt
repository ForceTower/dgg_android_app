package dev.forcetower.destiny.core.model.chat

data class ChatterConnection(
    val nick: String,
    val features: List<String>,
    val timestamp: Long
)