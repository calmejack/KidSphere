package com.kidsphere.game.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kidsphere.game.data.model.ChatMessage

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getBySession(sessionId: String): LiveData<List<ChatMessage>>
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getBySessionOnce(sessionId: String): List<ChatMessage>
    @Insert suspend fun insert(message: ChatMessage)
    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId") suspend fun clearSession(sessionId: String)
}
