package com.example.gameapp.domain.repository

import com.example.gameapp.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    // Получить список игр по API
    suspend fun getGames(page: Int, pageSize: Int): List<Game>

    // Получить детали об игре по API
    suspend fun getGamesDetail(gameId: Int): Game

    // Получить избранные игры с локльной БД
    fun getFavoriteGames(): Flow<List<Game>>

    // Добавить игру в избранное. Передаем всю игру а не ID,
    // чтобы не пришлось дополнительно подгружать игру через API
    suspend fun addToFavorites(game: Game)

    // Удалить из избранного
    suspend fun removeFromFavorites(gameId: Int)

    // Проверить, в избранном ли
    suspend fun isFavorite(gameId: Int): Boolean
}