package com.kidsphere.game.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: Int,          // 1 = easy, 2 = medium, 3 = hard
    val minAge: Int,
    val maxAge: Int,
    val iconUrl: String = "",
    val isUnlocked: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
