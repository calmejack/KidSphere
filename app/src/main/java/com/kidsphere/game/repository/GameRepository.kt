package com.kidsphere.game.repository

import com.kidsphere.game.data.local.dao.GameDao
import com.kidsphere.game.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val gameDao: GameDao
) {
    fun getAllGames(): Flow<List<GameEntity>> = gameDao.getAllGames()

    fun getGamesByCategory(category: String): Flow<List<GameEntity>> =
        gameDao.getGamesByCategory(category)

    fun getGamesForAge(age: Int): Flow<List<GameEntity>> =
        gameDao.getGamesForAge(age)

    suspend fun getGameById(id: Long): GameEntity? = gameDao.getGameById(id)

    suspend fun insertGame(game: GameEntity): Long = gameDao.insertGame(game)

    suspend fun insertGames(games: List<GameEntity>) = gameDao.insertGames(games)

    suspend fun updateGame(game: GameEntity) = gameDao.updateGame(game)

    suspend fun deleteGame(game: GameEntity) = gameDao.deleteGame(game)

    suspend fun seedDefaultGames() {
        val defaultGames = listOf(
            GameEntity(
                title = "Word Adventure",
                description = "Learn new words through fun storytelling with an AI guide!",
                category = "Language",
                difficulty = 1,
                minAge = 4,
                maxAge = 8
            ),
            GameEntity(
                title = "Math Quest",
                description = "Solve math puzzles with your AI companion to save the kingdom!",
                category = "Math",
                difficulty = 2,
                minAge = 6,
                maxAge = 12
            ),
            GameEntity(
                title = "Science Explorer",
                description = "Discover the wonders of science with interactive experiments!",
                category = "Science",
                difficulty = 2,
                minAge = 7,
                maxAge = 12
            ),
            GameEntity(
                title = "Story Creator",
                description = "Create amazing stories with your AI storytelling partner!",
                category = "Creativity",
                difficulty = 1,
                minAge = 5,
                maxAge = 10
            ),
            GameEntity(
                title = "Nature Explorer",
                description = "Learn about animals and nature through interactive adventures!",
                category = "Science",
                difficulty = 1,
                minAge = 4,
                maxAge = 9
            ),
            GameEntity(
                title = "Code Island",
                description = "Learn basic programming concepts in a fun island adventure!",
                category = "Technology",
                difficulty = 3,
                minAge = 8,
                maxAge = 14
            )
        )
        gameDao.insertGames(defaultGames)
    }
}
