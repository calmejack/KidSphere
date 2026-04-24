package com.kidsphere.game.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kidsphere.game.data.model.Quest
import com.kidsphere.game.data.model.QuestStatus

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests WHERE npcId = :npcId") fun getByNpc(npcId: String): LiveData<List<Quest>>
    @Query("SELECT * FROM quests WHERE id = :id") suspend fun getById(id: String): Quest?
    @Query("SELECT * FROM quests WHERE worldId = :worldId AND status = :status")
    fun getByWorldAndStatus(worldId: String, status: QuestStatus): LiveData<List<Quest>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(vararg quests: Quest)
    @Update suspend fun update(quest: Quest)
}
