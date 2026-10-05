package com.example.gameapp.domain.usecase

import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(private val repository: GameRepository) {
    suspend operator fun invoke(page: Int = 1, pageSize: Int = 20, search: String? = null): List<Game> {
        return repository.getGames(page, pageSize, search)
    }
}