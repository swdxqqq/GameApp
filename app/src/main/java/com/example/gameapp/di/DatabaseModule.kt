package com.example.gameapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gameapp.data.local.GameAppDatabase
import com.example.gameapp.data.local.dao.CustomGameDao
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

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `custom_games` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `name` TEXT NOT NULL,
                    `released` TEXT,
                    `backgroundImage` TEXT,
                    `rating` REAL NOT NULL,
                    `metacritic` INTEGER,
                    `playtime` INTEGER NOT NULL,
                    `genres` TEXT NOT NULL,
                    `addedAt` INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }

    @Singleton
    @Provides
    fun provideGameDatabase(
        @ApplicationContext context: Context
    ): GameAppDatabase{
        return Room.databaseBuilder(
            context,
            GameAppDatabase::class.java,
            "game_app_database"
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteGameDao(database: GameAppDatabase): FavoriteGameDao = database.favoriteGameDao()

    @Singleton
    @Provides
    fun provideCustomGameDao(database: GameAppDatabase): CustomGameDao = database.customGameDao()
}