package com.kidsphere.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidsphere.game.data.local.entity.NpcConversationEntity
import com.kidsphere.game.repository.AiRepository
import com.kidsphere.game.repository.TtsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NpcUiState(
    val isLoading: Boolean = false,
    val messages: List<NpcConversationEntity> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class NpcViewModel @Inject constructor(
    private val aiRepository: AiRepository,
    private val ttsRepository: TtsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NpcUiState())
    val uiState: StateFlow<NpcUiState> = _uiState.asStateFlow()

    fun sendMessage(
        npcName: String,
        npcPersonality: String,
        userMessage: String,
        useTts: Boolean = false
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val systemPrompt = buildSystemPrompt(npcName, npcPersonality)
            val result = aiRepository.chat(npcName, systemPrompt, userMessage)
            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                if (useTts) {
                    ttsRepository.speak(response)
                }
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Something went wrong"
                )
            }
        }
    }

    fun loadConversation(npcName: String) {
        viewModelScope.launch {
            aiRepository.getConversationHistory(npcName).collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun clearConversation(npcName: String) {
        viewModelScope.launch {
            aiRepository.clearConversation(npcName)
        }
    }

    private fun buildSystemPrompt(npcName: String, personality: String): String {
        return """
            You are $npcName, a friendly AI guide in KidSphere, an educational app for children.
            Personality: $personality
            Rules:
            - Keep responses short and age-appropriate (under 100 words)
            - Be encouraging and positive
            - Use simple vocabulary suitable for children ages 4-14
            - Never discuss adult topics or scary content
            - Focus on education and fun
            - Use emojis occasionally to make responses more engaging
        """.trimIndent()
    }

    override fun onCleared() {
        super.onCleared()
        ttsRepository.stop()
    }
}
