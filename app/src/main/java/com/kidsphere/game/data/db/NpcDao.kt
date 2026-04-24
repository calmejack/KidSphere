package com.kidsphere.game.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kidsphere.game.data.model.Npc

@Dao
interface NpcDao {
    @Query("SELECT * FROM npcs WHERE worldId = :worldId") fun getByWorld(worldId: String): LiveData<List<Npc>>
    @Query("SELECT * FROM npcs WHERE id = :id") suspend fun getById(id: String): Npc?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(vararg npcs: Npc)
    @Update suspend fun update(npc: Npc)
}
