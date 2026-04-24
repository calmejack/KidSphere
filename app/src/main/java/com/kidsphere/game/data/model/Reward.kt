package com.kidsphere.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RewardType { STAR, BADGE, ITEM, WORLD_UNLOCK }

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val type: RewardType = RewardType.STAR,
    val iconRes: String = "",
    val value: Int = 1,
    val isCollected: Boolean = false,
    val earnedAt: Long = 0L
)
