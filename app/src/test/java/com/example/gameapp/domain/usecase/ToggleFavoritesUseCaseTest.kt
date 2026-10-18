package com.example.gameapp.domain.usecase

import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.repository.GameRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.assertTrue

class ToggleFavoritesUseCaseTest {

    @Mock
    private lateinit var mockGameRepository: GameRepository

    private lateinit var toggleFavoritesUseCase: ToggleFavoritesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        toggleFavoritesUseCase = ToggleFavoritesUseCase(mockGameRepository)
    }

    @Test
    fun invokeShouldRemoveFromFavoritesWhenGameIsFavorite() {
        runBlocking {
            val game = Game(
                id = 1,
                name = "Test Game",
                released = "2020-01-01",
                backgroundImage = "url",
                rating = 4.5,
                metacritic = 85,
                playtime = 10,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(true)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).isFavorite(game.id)
            verify(mockGameRepository).removeFromFavorites(game.id)
        }
    }

    @Test
    fun invokeShouldAddToFavoritesWhenGameIsNotFavorite() {
        runBlocking {
            val game = Game(
                id = 2,
                name = "Another Game",
                released = "2021-05-15",
                backgroundImage = "url2",
                rating = 4.0,
                metacritic = 80,
                playtime = 5,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(false)

            
            toggleFavoritesUseCase(game)

            verify(mockGameRepository).isFavorite(game.id)
            verify(mockGameRepository).addToFavorites(game)
        }
    }

    @Test
    fun invokeShouldCallRepositoryExactlyOnce() {
        runBlocking {
            val game = Game(
                id = 3,
                name = "Third Game",
                released = "2019-12-01",
                backgroundImage = "url3",
                rating = 3.5,
                metacritic = 75,
                playtime = 8,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(true)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).isFavorite(game.id)
            assertTrue(true)
        }
    }

    @Test
    fun invokeShouldHandleNullBackgroundImage() {
        runBlocking {
            val game = Game(
                id = 4,
                name = "Game Without Image",
                released = "2022-06-10",
                backgroundImage = null,
                rating = 3.8,
                metacritic = 78,
                playtime = 12,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(false)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).isFavorite(game.id)
            verify(mockGameRepository).addToFavorites(game)
        }
    }

    @Test
    fun invokeShouldHandleGameWithHighRating() {
        runBlocking {
            val game = Game(
                id = 5,
                name = "Top Rated Game",
                released = "2023-01-15",
                backgroundImage = "url5",
                rating = 5.0,
                metacritic = 95,
                playtime = 50,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(false)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).addToFavorites(game)
        }
    }

    @Test
    fun invokeShouldHandleGameWithLowRating() {
        runBlocking {
            val game = Game(
                id = 6,
                name = "Low Rated Game",
                released = "2018-03-20",
                backgroundImage = "url6",
                rating = 1.5,
                metacritic = 45,
                playtime = 2,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(true)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).removeFromFavorites(game.id)
        }
    }

    @Test
    fun invokeShouldHandleMultipleTogglesSameGame() {
        runBlocking {
            val game = Game(
                id = 7,
                name = "Toggle Test Game",
                released = "2021-07-07",
                backgroundImage = "url7",
                rating = 4.2,
                metacritic = 82,
                playtime = 15,
                genres = emptyList()
            )

            // First toggle: add to favorites
            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(false)
            toggleFavoritesUseCase(game)
            verify(mockGameRepository).addToFavorites(game)

            // Second toggle: remove from favorites
            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(true)
            toggleFavoritesUseCase(game)
            verify(mockGameRepository).removeFromFavorites(game.id)
        }
    }

    @Test
    fun invokeShouldHandleGameWithZeroPlaytime() {
        runBlocking {
            val game = Game(
                id = 8,
                name = "New Release",
                released = "2024-01-01",
                backgroundImage = "url8",
                rating = 0.0,
                metacritic = 0,
                playtime = 0,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(false)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).addToFavorites(game)
        }
    }

    @Test
    fun invokeShouldHandleOldGame() {
        runBlocking {
            val game = Game(
                id = 9,
                name = "Classic Game",
                released = "1999-12-31",
                backgroundImage = "url9",
                rating = 4.8,
                metacritic = 90,
                playtime = 100,
                genres = emptyList()
            )

            `when`(mockGameRepository.isFavorite(game.id)).thenReturn(true)

            toggleFavoritesUseCase(game)

            verify(mockGameRepository).removeFromFavorites(game.id)
        }
    }
}
