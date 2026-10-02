package com.example.gameapp.domain.usecase

import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val repository: GameRepository) {
    operator fun invoke(): Flow<List<Game>> {
        return repository.getFavoriteGames()
    }
}