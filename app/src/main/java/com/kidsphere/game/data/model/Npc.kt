package com.kidsphere.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "npcs")
data class Npc(
    @PrimaryKey val id: String,
    val name: String,
    val worldId: String,
    val avatarRes: String = "",
    val personality: String = "friendly",   // used as system prompt hint
    val subject: String = "general",        // e.g., "math", "science", "language"
    val greetingText: String = "",
    val questIds: String = ""               // comma-separated quest ids
)
