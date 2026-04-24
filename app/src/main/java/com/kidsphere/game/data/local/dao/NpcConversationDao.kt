package com.kidsphere.game.data.local.dao

import androidx.room.*
import com.kidsphere.game.data.local.entity.NpcConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NpcConversationDao {

    @Query("SELECT * FROM npc_conversations WHERE npcName = :npcName ORDER BY timestamp ASC")
    fun getConversationHistory(npcName: String): Flow<List<NpcConversationEntity>>

    @Query("SELECT * FROM npc_conversations WHERE npcName = :npcName ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessages(npcName: String, limit: Int = 10): List<NpcConversationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: NpcConversationEntity): Long

    @Query("DELETE FROM npc_conversations WHERE npcName = :npcName")
    suspend fun clearConversation(npcName: String)

    @Query("DELETE FROM npc_conversations")
    suspend fun clearAllConversations()
}
