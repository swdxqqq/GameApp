package com.example.gameapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("results") val results: List<GameDto>
)

data class GameDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("released") val released: String?,
    @SerializedName("background_image") val backgroundImage: String?,
    @SerializedName("rating") val rating: Double,
    @SerializedName("metacritic") val metacritic: Int,
    @SerializedName("playtime") val playtime: Int,
    @SerializedName("genres") val genres: List<GenreDto>
)

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
