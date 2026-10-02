package com.example.gameapp.di

import android.content.Context
import androidx.room.Room
import com.example.gameapp.data.local.GameAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.gameapp.data.local.dao.FavoriteGameDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideGameDatabase(
        @ApplicationContext context: Context
    ): GameAppDatabase{
        return Room.databaseBuilder(
            context,
            GameAppDatabase::class.java,
            "game_app_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideFavoriteGameDao(database: GameAppDatabase): FavoriteGameDao = database.favoriteGameDao()
}