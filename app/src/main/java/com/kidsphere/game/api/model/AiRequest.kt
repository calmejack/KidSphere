package com.kidsphere.game.api.model

data class AiMessage(
    val role: String,
    val content: String
)

data class AiChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<AiMessage>,
    val max_tokens: Int = 512,
    val temperature: Float = 0.8f
)

data class AiChatChoice(
    val message: AiMessage,
    val finish_reason: String?
)

data class AiChatResponse(
    val choices: List<AiChatChoice>?
)
