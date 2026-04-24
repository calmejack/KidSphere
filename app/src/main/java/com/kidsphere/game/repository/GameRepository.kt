package com.kidsphere.game.repository

import com.kidsphere.game.data.db.*
import com.kidsphere.game.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(
    private val worldDao: WorldDao,
    private val npcDao: NpcDao,
    private val questDao: QuestDao,
    private val rewardDao: RewardDao,
    private val playerDao: PlayerDao
) {
    // --- Worlds ---
    fun getAllWorlds() = worldDao.getAll()
    suspend fun getAllWorldsOnce() = withContext(Dispatchers.IO) { worldDao.getAllOnce() }
    fun getUnlockedWorlds() = worldDao.getUnlocked()
    suspend fun unlockWorld(worldId: String) = withContext(Dispatchers.IO) {
        worldDao.getById(worldId)?.let { worldDao.update(it.copy(isUnlocked = true)) }
    }

    // --- NPCs ---
    fun getNpcsForWorld(worldId: String) = npcDao.getByWorld(worldId)
    suspend fun getNpcById(id: String) = withContext(Dispatchers.IO) { npcDao.getById(id) }

    // --- Quests ---
    fun getQuestsForNpc(npcId: String) = questDao.getByNpc(npcId)
    suspend fun getQuestById(id: String) = withContext(Dispatchers.IO) { questDao.getById(id) }

    /** Atomically marks quest complete and updates player stats. */
    suspend fun completeQuestAndAwardPlayer(quest: Quest) = withContext(Dispatchers.IO) {
        questDao.update(quest.copy(status = QuestStatus.COMPLETED, completionCount = quest.completionCount + 1))
        val p = playerDao.getPlayerOnce() ?: Player()
        val completedIds = if (p.completedQuestIds.isEmpty()) quest.id
                           else "${p.completedQuestIds},${quest.id}"
        val newXp = p.xp + quest.rewardStars * 10
        playerDao.update(p.copy(
            totalStars       = p.totalStars + quest.rewardStars,
            xp               = newXp,
            level            = 1 + newXp / 100,
            completedQuestIds = completedIds
        ))
        if (quest.rewardItemId.isNotBlank()) {
            rewardDao.getById(quest.rewardItemId)?.let {
                rewardDao.update(it.copy(isCollected = true, earnedAt = System.currentTimeMillis()))
            }
        }
    }

    // --- Seed initial data ---
    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        if (playerDao.getPlayerOnce() == null) {
            playerDao.insert(Player())
            seedWorlds()
            seedNpcs()
            seedQuests()
            seedRewards()
        }
    }

    private suspend fun seedWorlds() {
        worldDao.insertAll(
            World("world_forest",  "Enchanted Forest",  "A magical forest full of talking animals", "forest",  true,  0),
            World("world_ocean",   "Deep Ocean",        "Explore the mysterious deep sea",          "ocean",   false, 5),
            World("world_space",   "Outer Space",       "Journey through the stars and planets",    "space",   false, 15),
            World("world_ancient", "Ancient Ruins",     "Discover ancient civilizations",           "ancient", false, 30)
        )
    }

    private suspend fun seedNpcs() {
        npcDao.insertAll(
            Npc("npc_owl",      "Ollie the Owl",      "world_forest",  personality = "wise and patient",    subject = "math"),
            Npc("npc_rabbit",   "Ruby the Rabbit",    "world_forest",  personality = "energetic and fun",   subject = "language"),
            Npc("npc_dolphin",  "Delphi the Dolphin", "world_ocean",   personality = "playful and curious", subject = "science"),
            Npc("npc_crab",     "Captain Crab",       "world_ocean",   personality = "adventurous",         subject = "geography"),
            Npc("npc_robot",    "RoboNova",           "world_space",   personality = "logical and precise", subject = "science"),
            Npc("npc_alien",    "Zara the Alien",     "world_space",   personality = "mysterious and fun",  subject = "astronomy"),
            Npc("npc_pharaoh",  "Pharaoh Pip",        "world_ancient", personality = "regal and kind",      subject = "history"),
            Npc("npc_sphinx",   "Sphinx Sasha",       "world_ancient", personality = "riddle-loving",       subject = "logic")
        )
    }

    private suspend fun seedQuests() {
        questDao.insertAll(
            Quest("q_owl_1",     "npc_owl",     "world_forest",  "Counting Acorns",   "Help Ollie count acorns for winter!",          QuestType.QUIZ,   rewardStars = 2, knowledgeTag = "math"),
            Quest("q_owl_2",     "npc_owl",     "world_forest",  "Shape Patrol",      "Find all the shapes hiding in the forest!",    QuestType.PUZZLE, rewardStars = 3, knowledgeTag = "geometry"),
            Quest("q_rabbit_1",  "npc_rabbit",  "world_forest",  "Story Time",        "Listen and answer questions about the story!", QuestType.STORY,  rewardStars = 2, knowledgeTag = "reading"),
            Quest("q_dolphin_1", "npc_dolphin", "world_ocean",   "Ocean Explorer",    "Learn about sea creatures!",                   QuestType.QUIZ,   rewardStars = 3, knowledgeTag = "science"),
            Quest("q_crab_1",    "npc_crab",    "world_ocean",   "Map the Seas",      "Help Captain Crab draw a map!",                QuestType.CRAFT,  rewardStars = 4, knowledgeTag = "geography"),
            Quest("q_robot_1",   "npc_robot",   "world_space",   "Star Patterns",     "Spot the constellations!",                     QuestType.PUZZLE, rewardStars = 3, knowledgeTag = "astronomy"),
            Quest("q_alien_1",   "npc_alien",   "world_space",   "Alien Numbers",     "Count in different number bases!",             QuestType.QUIZ,   rewardStars = 4, knowledgeTag = "math"),
            Quest("q_pharaoh_1", "npc_pharaoh", "world_ancient", "Hieroglyphic Fun",  "Decode ancient messages!",                     QuestType.PUZZLE, rewardStars = 5, knowledgeTag = "history")
        )
    }

    private suspend fun seedRewards() {
        rewardDao.insertAll(
            Reward("r_star_1",  "Gold Star",      "Complete your first quest!",     RewardType.STAR,         value = 1),
            Reward("r_badge_1", "Forest Hero",    "Complete all forest quests",     RewardType.BADGE,        value = 5),
            Reward("r_badge_2", "Ocean Diver",    "Complete all ocean quests",      RewardType.BADGE,        value = 5),
            Reward("r_badge_3", "Space Cadet",    "Complete all space quests",      RewardType.BADGE,        value = 10),
            Reward("r_badge_4", "History Buff",   "Complete all ancient quests",    RewardType.BADGE,        value = 10),
            Reward("r_world_2", "Ocean Unlock",   "Unlocked the Deep Ocean world!", RewardType.WORLD_UNLOCK, value = 0),
            Reward("r_world_3", "Space Unlock",   "Unlocked Outer Space world!",    RewardType.WORLD_UNLOCK, value = 0),
            Reward("r_world_4", "Ancient Unlock", "Unlocked the Ancient Ruins!",    RewardType.WORLD_UNLOCK, value = 0)
        )
    }
}
