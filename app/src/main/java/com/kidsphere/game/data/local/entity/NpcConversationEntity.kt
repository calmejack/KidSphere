package com.kidsphere.game.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "npc_conversations")
data class NpcConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val npcName: String,
    val userMessage: String,
    val npcResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)
