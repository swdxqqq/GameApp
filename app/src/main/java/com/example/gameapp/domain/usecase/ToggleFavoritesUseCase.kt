package com.example.gameapp.domain.usecase

import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import javax.inject.Inject

class ToggleFavoritesUseCase @Inject constructor (private val repository: GameRepository) {
    suspend operator fun invoke(game: Game){
        if(repository.isFavorite(game.id)){
            repository.removeFromFavorites(game.id)
        }
        else{
            repository.addToFavorites(game)
        }
    }
}