package com.kidsphere.game.repository

import com.kidsphere.game.api.ApiKeyManager
import com.kidsphere.game.api.RetrofitClient
import com.kidsphere.game.api.model.AiChatRequest
import com.kidsphere.game.api.model.AiMessage
import com.kidsphere.game.data.db.ChatMessageDao
import com.kidsphere.game.data.model.ChatMessage
import com.kidsphere.game.data.model.MessageRole
import com.kidsphere.game.data.model.Npc

class AiRepository(
    private val chatMessageDao: ChatMessageDao,
    private val keyManager: ApiKeyManager
) {

    suspend fun chat(
        npc: Npc,
        sessionId: String,
        userText: String
    ): Result<String> {
        // Persist user message
        chatMessageDao.insert(
            ChatMessage(sessionId = sessionId, role = MessageRole.USER, content = userText)
        )

        val history = chatMessageDao.getBySessionOnce(sessionId)

        val systemPrompt = buildSystemPrompt(npc)
        val messages = mutableListOf(AiMessage("system", systemPrompt))
        history.forEach { msg ->
            messages.add(AiMessage(msg.role.name.lowercase(), msg.content))
        }

        return try {
            val service = RetrofitClient.buildAiService(keyManager.aiBaseUrl)
            val response = service.chat(
                authorization = "Bearer ${keyManager.aiApiKey}",
                request = AiChatRequest(
                    model = keyManager.aiModel,
                    messages = messages
                )
            )
            if (response.isSuccessful) {
                val reply = response.body()?.choices?.firstOrNull()?.message?.content
                    ?: "Sorry, I didn't catch that. Can you try asking again?"
                chatMessageDao.insert(
                    ChatMessage(sessionId = sessionId, role = MessageRole.ASSISTANT, content = reply)
                )
                Result.success(reply)
            } else {
                Result.failure(Exception("AI API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildSystemPrompt(npc: Npc): String {
        return """You are ${npc.name}, a ${npc.personality} character in a children's educational game called KidSphere.
Your subject area is ${npc.subject}. You talk to children aged 5-10 years old.
Keep responses short (2-3 sentences), fun, encouraging, and age-appropriate.
After chatting briefly, present a simple question or task related to ${npc.subject}.
Always be positive and never use complex vocabulary."""
    }

    fun getSessionMessages(sessionId: String) = chatMessageDao.getBySession(sessionId)
}
