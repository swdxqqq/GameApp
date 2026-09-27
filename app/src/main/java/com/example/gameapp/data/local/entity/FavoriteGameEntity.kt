package com.example.gameapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Сущность для локального хранения избранных игр
@Entity(tableName = "favorite_games")
data class FavoriteGameEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val released: String?,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val playtime: Int,
    val genres: String, // Сохраняем список жанров в виде одной строки
    val addedAt: Long = System.currentTimeMillis()
)