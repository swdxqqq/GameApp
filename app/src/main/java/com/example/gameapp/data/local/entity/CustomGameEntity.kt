package com.example.gameapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gameapp.domain.model.Game

@Entity(tableName = "custom_games")
data class CustomGameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val released: String?,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val playtime: Int,
    val genres: String, // Хранится как строка через запятую
    val addedAt: Long = System.currentTimeMillis()
)

fun CustomGameEntity.toDomain(): Game {
    return Game(
        id = id,
        name = name,
        released = released,
        backgroundImage = backgroundImage,
        rating = rating,
        metacritic = metacritic,
        playtime = playtime,
        genres = genres.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    )
}

fun Game.toCustomEntity(): CustomGameEntity {
    return CustomGameEntity(
        id = id,
        name = name,
        released = released,
        backgroundImage = backgroundImage,
        rating = rating,
        metacritic = metacritic,
        playtime = playtime,
        genres = genres.joinToString(",")
    )
}
