package com.kidsphere.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey val id: String = "local_player",
    val name: String = "Explorer",
    val avatarRes: String = "",
    val totalStars: Int = 0,
    val currentWorldId: String = "world_forest",
    val completedQuestIds: String = "",   // comma-separated
    val collectedRewardIds: String = "",  // comma-separated
    val level: Int = 1,
    val xp: Int = 0
)
