package com.example.gameapp.domain.model

// Модель исключительно для бизнес логики - не зависит от Room, Retrofit, UI
data class Game(
    val id: Int,
    val name: String,
    val released: String?,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val playtime: Int,
    val genres: List<String>
)