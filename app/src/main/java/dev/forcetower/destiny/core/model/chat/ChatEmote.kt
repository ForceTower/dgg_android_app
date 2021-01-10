package dev.forcetower.destiny.core.model.chat

data class ChatEmote(
    val prefix: String,
    val image: List<ChatImage>
)

data class ChatImage(
    val url: String,
    val name: String,
    val width: Int,
    val height: Int
)
