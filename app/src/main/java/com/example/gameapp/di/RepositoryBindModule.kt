package com.example.gameapp.di

import com.example.gameapp.data.repository.GameRepositoryImpl
import com.example.gameapp.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Singleton
    @Binds
    abstract fun bindGameRepository(
        impl: GameRepositoryImpl
    ) : GameRepository
}