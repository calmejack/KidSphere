package com.kidsphere.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuestType { QUIZ, STORY, PUZZLE, CRAFT }
enum class QuestStatus { LOCKED, AVAILABLE, IN_PROGRESS, COMPLETED }

@Entity(tableName = "quests")
data class Quest(
    @PrimaryKey val id: String,
    val npcId: String,
    val worldId: String,
    val title: String,
    val description: String,
    val type: QuestType = QuestType.QUIZ,
    val status: QuestStatus = QuestStatus.AVAILABLE,
    val rewardStars: Int = 1,
    val rewardItemId: String = "",
    val knowledgeTag: String = "",           // topic/subject tag
    val completionCount: Int = 0
)
