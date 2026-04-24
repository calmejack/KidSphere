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

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    init {
        viewModelScope.launch {
            gameRepo.seedIfEmpty()
        }
    }

    fun getNpcsForWorld(worldId: String) = gameRepo.getNpcsForWorld(worldId)

    fun getQuestsForNpc(npcId: String) = gameRepo.getQuestsForNpc(npcId)

    fun onQuestCompleted(quest: Quest) {
        viewModelScope.launch {
            gameRepo.completeQuestAndAwardPlayer(quest)
            checkWorldUnlocks()
            _toastMessage.postValue("🌟 Quest complete! +${quest.rewardStars} stars")
        }
    }

    private suspend fun checkWorldUnlocks() {
        val p = gameRepo.getPlayer() ?: return
        // World unlock thresholds are stored on the World entity (requiredStars field)
        val allWorlds = gameRepo.getAllWorldsOnce()
        for (world in allWorlds) {
            if (!world.isUnlocked && p.totalStars >= world.requiredStars && world.requiredStars > 0) {
                gameRepo.unlockWorld(world.id)
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
