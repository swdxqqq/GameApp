package com.example.gameapp.data.remote

import com.example.gameapp.data.remote.dto.GameDto
import com.example.gameapp.data.remote.dto.GameListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApi {
    // Получить список игр с пагинацией - разделение на страницы
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): GameListResponse

    // Получить данные об игре
    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GameDto
}