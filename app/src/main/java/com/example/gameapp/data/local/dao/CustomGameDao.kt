package com.example.gameapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gameapp.data.local.entity.CustomGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomGameDao {
    @Query("SELECT * FROM custom_games ORDER BY addedAt DESC")
    fun getAllCustomGames(): Flow<List<CustomGameEntity>>

    @Insert
    suspend fun addCustomGame(game: CustomGameEntity)

    @Query("DELETE FROM custom_games WHERE id = :gameId")
    suspend fun deleteCustomGame(gameId: Int)
}
