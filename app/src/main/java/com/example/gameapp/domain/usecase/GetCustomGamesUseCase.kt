package com.example.gameapp.domain.usecase

import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<List<Game>> = gameRepository.getCustomGames()
}