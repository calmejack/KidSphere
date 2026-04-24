package com.kidsphere.game.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kidsphere.game.data.model.World

@Dao
interface WorldDao {
    @Query("SELECT * FROM worlds") fun getAll(): LiveData<List<World>>
    @Query("SELECT * FROM worlds") suspend fun getAllOnce(): List<World>
    @Query("SELECT * FROM worlds WHERE id = :id") suspend fun getById(id: String): World?
    @Query("SELECT * FROM worlds WHERE isUnlocked = 1") fun getUnlocked(): LiveData<List<World>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(vararg worlds: World)
    @Update suspend fun update(world: World)
}
