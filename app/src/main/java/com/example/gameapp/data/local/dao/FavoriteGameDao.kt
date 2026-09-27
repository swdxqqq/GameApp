package com.example.gameapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gameapp.data.local.entity.FavoriteGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGameDao {
    // Получить все избранные игры
    @Query("SELECT * FROM favorite_games ORDER BY addedAt DESC")
    fun getFavoritesGames(): Flow<List<FavoriteGameEntity>>

    // Проверить есть ли игра в избранном
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_games WHERE id = :gameId)")
    suspend fun isFavorite(gameId: Int): Boolean

    //Добавить игру в избранное
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(game: FavoriteGameEntity)

    // Удалить из избранного
    @Query("DELETE FROM favorite_games WHERE id = :gameId")
    suspend fun removeFromFavorites(gameId: Int)
}