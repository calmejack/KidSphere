package com.kidsphere.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worlds")
data class World(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val theme: String,           // e.g., "forest", "ocean", "space", "ancient"
    val isUnlocked: Boolean = false,
    val requiredStars: Int = 0,  // stars needed to unlock
    val backgroundRes: String = "",
    val npcIds: String = ""      // comma-separated NPC ids
)
