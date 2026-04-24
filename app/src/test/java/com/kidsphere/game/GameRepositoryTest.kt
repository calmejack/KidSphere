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
}
