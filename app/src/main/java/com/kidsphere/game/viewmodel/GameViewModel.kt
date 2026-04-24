package com.kidsphere.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidsphere.game.data.local.entity.GameEntity
import com.kidsphere.game.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    val allGames: StateFlow<List<GameEntity>> = gameRepository.getAllGames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getGamesByCategory(category: String): StateFlow<List<GameEntity>> =
        gameRepository.getGamesByCategory(category)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getGamesForAge(age: Int): StateFlow<List<GameEntity>> =
        gameRepository.getGamesForAge(age)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seedGames() {
        viewModelScope.launch {
            gameRepository.seedDefaultGames()
        }
    }

    fun deleteGame(game: GameEntity) {
        viewModelScope.launch {
            gameRepository.deleteGame(game)
        }
    }
}
