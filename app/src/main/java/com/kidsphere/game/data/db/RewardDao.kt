package com.kidsphere.game.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kidsphere.game.data.model.Reward

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards") fun getAll(): LiveData<List<Reward>>
    @Query("SELECT * FROM rewards WHERE isCollected = 1") fun getCollected(): LiveData<List<Reward>>
    @Query("SELECT * FROM rewards WHERE id = :id") suspend fun getById(id: String): Reward?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(vararg rewards: Reward)
    @Update suspend fun update(reward: Reward)
}
