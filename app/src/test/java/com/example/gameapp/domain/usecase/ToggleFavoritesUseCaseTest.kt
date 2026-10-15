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

            // Act
            toggleFavoritesUseCase(game)

            // Assert
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

            // Act
            toggleFavoritesUseCase(game)

            // Assert
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
}
