package com.kidsphere.game.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kidsphere.game.data.model.Player

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE id = 'local_player' LIMIT 1") fun getPlayer(): LiveData<Player?>
    @Query("SELECT * FROM players WHERE id = 'local_player' LIMIT 1") suspend fun getPlayerOnce(): Player?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(player: Player)
    @Update suspend fun update(player: Player)
}
