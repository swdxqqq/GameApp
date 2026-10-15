package com.example.gameapp.domain.usecase

import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import javax.inject.Inject

class AddCustomGameUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(game: Game): Result<Unit> = runCatching {
        gameRepository.addCustomGame(game)
    }
}