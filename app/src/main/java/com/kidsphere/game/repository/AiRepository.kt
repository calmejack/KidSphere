package com.kidsphere.game.repository

import com.kidsphere.game.data.local.dao.NpcConversationDao
import com.kidsphere.game.data.local.entity.NpcConversationEntity
import com.kidsphere.game.data.remote.api.OpenAiService
import com.kidsphere.game.data.remote.model.ChatMessage
import com.kidsphere.game.data.remote.model.ChatRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val openAiService: OpenAiService,
    private val npcConversationDao: NpcConversationDao
) {
    fun getConversationHistory(npcName: String): Flow<List<NpcConversationEntity>> =
        npcConversationDao.getConversationHistory(npcName)

    suspend fun chat(npcName: String, systemPrompt: String, userMessage: String): Result<String> {
        return try {
            val recentHistory = npcConversationDao.getRecentMessages(npcName, 5)
            val messages = mutableListOf<ChatMessage>()
            messages.add(ChatMessage("system", systemPrompt))
            recentHistory.forEach { msg ->
                messages.add(ChatMessage("user", msg.userMessage))
                messages.add(ChatMessage("assistant", msg.npcResponse))
            }
            messages.add(ChatMessage("user", userMessage))

            val response = openAiService.chat(ChatRequest(messages = messages))
            if (response.isSuccessful) {
                val aiResponse = response.body()?.choices?.firstOrNull()?.message?.content
                    ?: "I'm not sure what to say..."
                // Save to history
                npcConversationDao.insertMessage(
                    NpcConversationEntity(
                        npcName = npcName,
                        userMessage = userMessage,
                        npcResponse = aiResponse
                    )
                )
                Result.success(aiResponse)
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearConversation(npcName: String) {
        npcConversationDao.clearConversation(npcName)
    }
}
