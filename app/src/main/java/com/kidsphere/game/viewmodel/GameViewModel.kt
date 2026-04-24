package com.kidsphere.game.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.kidsphere.game.api.ApiKeyManager
import com.kidsphere.game.data.db.AppDatabase
import com.kidsphere.game.data.model.*
import com.kidsphere.game.repository.GameRepository
import com.kidsphere.game.repository.TtsRepository
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val keyManager = ApiKeyManager(application)
    private val gameRepo = GameRepository(
        db.worldDao(), db.npcDao(), db.questDao(), db.rewardDao(), db.playerDao()
    )
    private val ttsRepo = TtsRepository(application, keyManager)

    val player: LiveData<Player?> = gameRepo.getPlayerLive()
    val allWorlds: LiveData<List<World>> = gameRepo.getAllWorlds()
    val unlockedWorlds: LiveData<List<World>> = gameRepo.getUnlockedWorlds()
    val collectedRewards: LiveData<List<Reward>> = gameRepo.getCollectedRewards()

    private val _currentWorldId = MutableLiveData<String>("world_forest")
    val currentWorldId: LiveData<String> = _currentWorldId

    private val _npcsInWorld = MutableLiveData<List<Npc>>()
    val npcsInWorld: LiveData<List<Npc>> = _npcsInWorld

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    init {
        viewModelScope.launch {
            gameRepo.seedIfEmpty()
        }
    }

    fun loadNpcsForWorld(worldId: String) {
        _currentWorldId.value = worldId
        gameRepo.getNpcsForWorld(worldId).observeForever {
            _npcsInWorld.value = it
        }
    }

    fun getQuestsForNpc(npcId: String) = gameRepo.getQuestsForNpc(npcId)

    fun onQuestCompleted(quest: Quest) {
        viewModelScope.launch {
            gameRepo.completeQuest(quest.id)
            gameRepo.markQuestCompleted(quest.id)
            gameRepo.addStars(quest.rewardStars)
            gameRepo.addXp(quest.rewardStars * 10)
            if (quest.rewardItemId.isNotBlank()) {
                gameRepo.collectReward(quest.rewardItemId)
            }
            checkWorldUnlocks()
            _toastMessage.postValue("🌟 Quest complete! +${quest.rewardStars} stars")
        }
    }

    private suspend fun checkWorldUnlocks() {
        val p = gameRepo.getPlayer() ?: return
        val worlds = listOf(
            Triple("world_ocean",   5,  "r_world_2"),
            Triple("world_space",   15, "r_world_3"),
            Triple("world_ancient", 30, "r_world_4")
        )
        for ((worldId, required, rewardId) in worlds) {
            if (p.totalStars >= required) {
                gameRepo.unlockWorld(worldId)
                gameRepo.collectReward(rewardId)
            }
        }
    }

    fun speakText(text: String) {
        viewModelScope.launch {
            ttsRepo.speakText(text)
        }
    }

    fun clearToast() { _toastMessage.value = null }
}
