package com.kidsphere.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageRole { USER, ASSISTANT, SYSTEM }

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,         // npcId + questId combo
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
