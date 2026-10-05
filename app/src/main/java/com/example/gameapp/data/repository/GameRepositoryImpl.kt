package com.example.gameapp.data.repository

import com.example.gameapp.data.local.dao.FavoriteGameDao
import com.example.gameapp.data.local.entity.toDomain
import com.example.gameapp.data.local.entity.toEntity
import com.example.gameapp.data.remote.RawgApi
import com.example.gameapp.data.remote.dto.toDomain
import com.example.gameapp.di.ApiKey
import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val api: RawgApi,
    private val dao: FavoriteGameDao,
    @ApiKey
    private val apiKey: String
) : GameRepository {

    override suspend fun getGames(page: Int, pageSize: Int, search: String?): List<Game>{
        val response = api.getGames(apiKey, page, pageSize, search = search)
        return response.results.map { it.toDomain()}
    }

    override suspend fun getGameDetail(gameId: Int): Game {
        val dto = api.getGameDetail(gameId, apiKey)
        return dto.toDomain()
    }

    override fun getFavoriteGames(): Flow<List<Game>> {
        return dao.getFavoriteGames().map {
            entities -> entities.map { it.toDomain() }
        }
    }

    override suspend fun addToFavorites(game: Game) {
        val entity = game.toEntity()
        dao.addToFavorites(entity)
    }

    override suspend fun removeFromFavorites(gameId: Int) {
        dao.removeFromFavorites(gameId)
    }

    override suspend fun isFavorite(gameId: Int): Boolean {
        return dao.isFavorite(gameId)
    }
}