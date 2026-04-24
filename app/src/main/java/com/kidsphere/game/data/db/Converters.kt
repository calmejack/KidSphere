package com.kidsphere.game.data.db

import androidx.room.TypeConverter
import com.kidsphere.game.data.model.MessageRole
import com.kidsphere.game.data.model.QuestStatus
import com.kidsphere.game.data.model.QuestType
import com.kidsphere.game.data.model.RewardType

class Converters {
    @TypeConverter fun fromQuestType(v: QuestType): String = v.name
    @TypeConverter fun toQuestType(v: String): QuestType = QuestType.valueOf(v)
    @TypeConverter fun fromQuestStatus(v: QuestStatus): String = v.name
    @TypeConverter fun toQuestStatus(v: String): QuestStatus = QuestStatus.valueOf(v)
    @TypeConverter fun fromRewardType(v: RewardType): String = v.name
    @TypeConverter fun toRewardType(v: String): RewardType = RewardType.valueOf(v)
    @TypeConverter fun fromMessageRole(v: MessageRole): String = v.name
    @TypeConverter fun toMessageRole(v: String): MessageRole = MessageRole.valueOf(v)
}
