package com.kidsphere.game.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kidsphere.game.data.model.*

@Database(
    entities = [World::class, Npc::class, Quest::class, Reward::class, Player::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun worldDao(): WorldDao
    abstract fun npcDao(): NpcDao
    abstract fun questDao(): QuestDao
    abstract fun rewardDao(): RewardDao
    abstract fun playerDao(): PlayerDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "kidsphere.db"
                ).build().also { INSTANCE = it }
            }
    }
}
