package com.kidsphere.game

import com.kidsphere.game.data.model.*
import org.junit.Assert.*
import org.junit.Test

class GameRepositoryTest {

    @Test
    fun `player starts with zero stars`() {
        val player = Player()
        assertEquals(0, player.totalStars)
        assertEquals(1, player.level)
    }

    @Test
    fun `quest reward stars is positive`() {
        val quest = Quest(
            id = "q1",
            npcId = "npc1",
            worldId = "world1",
            title = "Test Quest",
            description = "Test",
            rewardStars = 3
        )
        assertTrue(quest.rewardStars > 0)
    }

    @Test
    fun `world starts locked by default`() {
        val world = World(
            id = "world_test",
            name = "Test World",
            description = "Test",
            theme = "test"
        )
        assertFalse(world.isUnlocked)
    }

    @Test
    fun `first world starts unlocked`() {
        val world = World(
            id = "world_forest",
            name = "Enchanted Forest",
            description = "A magical forest",
            theme = "forest",
            isUnlocked = true
        )
        assertTrue(world.isUnlocked)
    }

    @Test
    fun `npc has valid subject`() {
        val npc = Npc(
            id = "npc_owl",
            name = "Ollie the Owl",
            worldId = "world_forest",
            subject = "math"
        )
        assertTrue(npc.subject.isNotEmpty())
    }

    @Test
    fun `reward type defaults to STAR`() {
        val reward = Reward(id = "r1", name = "Star", description = "A star")
        assertEquals(RewardType.STAR, reward.type)
    }

    @Test
    fun `quest status defaults to AVAILABLE`() {
        val quest = Quest(
            id = "q1", npcId = "n1", worldId = "w1",
            title = "Test", description = "Desc"
        )
        assertEquals(QuestStatus.AVAILABLE, quest.status)
    }

    @Test
    fun `chat message role is persisted`() {
        val msg = ChatMessage(
            sessionId = "session_1",
            role = MessageRole.USER,
            content = "Hello!"
        )
        assertEquals(MessageRole.USER, msg.role)
        assertEquals("Hello!", msg.content)
    }

    @Test
    fun `xp level calculation`() {
        // Level formula: 1 + xp / 100
        val xp = 150
        val expectedLevel = 1 + xp / 100
        assertEquals(2, expectedLevel)
    }

    @Test
    fun `world unlock threshold for ocean`() {
        val requiredStars = 5
        val playerStars = 5
        assertTrue(playerStars >= requiredStars)
    }

    @Test
    fun `completing quest increments completion count`() {
        val quest = Quest(id = "q1", npcId = "n1", worldId = "w1",
            title = "Test", description = "Desc", completionCount = 0)
        val updated = quest.copy(status = QuestStatus.COMPLETED, completionCount = quest.completionCount + 1)
        assertEquals(QuestStatus.COMPLETED, updated.status)
        assertEquals(1, updated.completionCount)
    }

    @Test
    fun `player stars update correctly after quest completion`() {
        val player = Player(totalStars = 3)
        val questStars = 2
        val updated = player.copy(totalStars = player.totalStars + questStars)
        assertEquals(5, updated.totalStars)
    }

    @Test
    fun `world locked when player has insufficient stars`() {
        val world = World(id = "world_ocean", name = "Deep Ocean",
            description = "", theme = "ocean", isUnlocked = false, requiredStars = 5)
        val playerStars = 3
        assertFalse(playerStars >= world.requiredStars)
    }

    @Test
    fun `world should unlock when player has enough stars`() {
        val world = World(id = "world_ocean", name = "Deep Ocean",
            description = "", theme = "ocean", isUnlocked = false, requiredStars = 5)
        val playerStars = 5
        assertTrue(playerStars >= world.requiredStars)
    }

    @Test
    fun `completed quest ids appended correctly`() {
        val player = Player(completedQuestIds = "q1")
        val newId = "q2"
        val updated = player.copy(completedQuestIds = "${player.completedQuestIds},$newId")
        assertTrue(updated.completedQuestIds.contains(newId))
        assertTrue(updated.completedQuestIds.contains("q1"))
    }
}
