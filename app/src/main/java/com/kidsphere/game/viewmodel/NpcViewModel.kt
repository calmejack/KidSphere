package com.kidsphere.game.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.kidsphere.game.api.ApiKeyManager
import com.kidsphere.game.data.db.AppDatabase
import com.kidsphere.game.data.model.ChatMessage
import com.kidsphere.game.data.model.Npc
import com.kidsphere.game.repository.AiRepository
import com.kidsphere.game.repository.TtsRepository
import kotlinx.coroutines.launch

class NpcViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val keyManager = ApiKeyManager(application)
    private val aiRepo = AiRepository(db.chatMessageDao(), keyManager)
    private val ttsRepo = TtsRepository(application, keyManager)

    private val _npc = MutableLiveData<Npc?>()
    val npc: LiveData<Npc?> = _npc

    private val _sessionId = MutableLiveData<String>()

    private val _messages: MediatorLiveData<List<ChatMessage>> = MediatorLiveData()
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setNpc(npc: Npc, questId: String = "") {
        _npc.value = npc
        val sid = "${npc.id}_${questId}"
        _sessionId.value = sid
        val src = aiRepo.getSessionMessages(sid)
        _messages.addSource(src) { _messages.value = it }
    }

    fun sendMessage(text: String) {
        val npc = _npc.value ?: return
        val sid = _sessionId.value ?: return
        _isLoading.value = true
        viewModelScope.launch {
            val result = aiRepo.chat(npc, sid, text)
            _isLoading.postValue(false)
            result.onFailure { _errorMessage.postValue(it.message) }
            result.onSuccess { reply ->
                ttsRepo.speakText(reply)
            }
        }
    }

    fun clearError() { _errorMessage.value = null }
}
