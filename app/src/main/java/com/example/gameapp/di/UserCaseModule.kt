package com.example.gameapp.di

import com.example.gameapp.domain.repository.GameRepository
import com.example.gameapp.domain.usecase.GetFavoritesUseCase
import com.example.gameapp.domain.usecase.GetGamesUseCase
import com.example.gameapp.domain.usecase.ToggleFavoritesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserCaseModule {

    @Singleton
    @Provides
    fun provideGetGamesUseCase(repository: GameRepository): GetGamesUseCase{
        return GetGamesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetFavoritesUseCase(repository: GameRepository): GetFavoritesUseCase{
        return GetFavoritesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideToggleFavoritesUseCase(repository: GameRepository): ToggleFavoritesUseCase {
        return ToggleFavoritesUseCase(repository)
    }
}