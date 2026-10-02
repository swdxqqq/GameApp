package com.example.gameapp.di

import com.example.gameapp.data.repository.GameRepositoryImpl
import com.example.gameapp.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    companion object {
        private const val RAWG_API_KEY = "b7fb2da99f5d46e1a734772ba203c9ec"
        @Qualifier
        @Retention(AnnotationRetention.BINARY)
        annotation class ApiKey

        @Singleton
        @Provides
        @ApiKey
        fun provideApiKey(): String = RAWG_API_KEY
    }

    @Singleton
    @Binds
    abstract fun bindGameRepository(
        impl: GameRepositoryImpl
    ): GameRepository
}