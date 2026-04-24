package com.kidsphere.game.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kidsphere.game.data.local.dao.GameDao
import com.kidsphere.game.data.local.dao.NpcConversationDao
import com.kidsphere.game.data.local.entity.GameEntity
import com.kidsphere.game.data.local.entity.NpcConversationEntity

@Database(
    entities = [GameEntity::class, NpcConversationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KidSphereDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun npcConversationDao(): NpcConversationDao

    companion object {
        const val DATABASE_NAME = "kidsphere.db"
    }
}
