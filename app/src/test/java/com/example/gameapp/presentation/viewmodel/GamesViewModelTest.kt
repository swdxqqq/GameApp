package com.example.gameapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.usecase.AddCustomGameUseCase
import com.example.gameapp.domain.usecase.GetCustomGamesUseCase
import com.example.gameapp.domain.usecase.GetFavoritesUseCase
import com.example.gameapp.domain.usecase.GetGamesUseCase
import com.example.gameapp.domain.usecase.ToggleFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GamesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetGamesUseCase: GetGamesUseCase = mock()
    private val mockGetFavoritesUseCase: GetFavoritesUseCase = mock()
    private val mockToggleFavoritesUseCase: ToggleFavoritesUseCase = mock()
    private val mockGetCustomGamesUseCase: GetCustomGamesUseCase = mock()
    private val mockAddCustomGameUseCase: AddCustomGameUseCase = mock()

    private lateinit var viewModel: GamesViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        whenever(mockGetFavoritesUseCase()).thenReturn(flowOf(emptyList()))
        whenever(mockGetCustomGamesUseCase()).thenReturn(flowOf(emptyList()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialStateShouldBeCorrect() = runTest(testDispatcher) {
        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase
        )

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.games.isEmpty())
        assertTrue(state.favorites.isEmpty())
        assertTrue(state.customGames.isEmpty())
        assertFalse(state.showAddGameDialog)
        assertEquals(1, state.currentPage)
    }

    @Test
    fun toggleAddGameDialogShouldChangeDialogState() = runTest(testDispatcher) {
        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.toggleAddGameDialog()
        assertTrue(viewModel.uiState.value.showAddGameDialog)

        viewModel.toggleAddGameDialog()
        assertFalse(viewModel.uiState.value.showAddGameDialog)
    }

    @Test
    fun addCustomGameShouldCallUseCaseAndCloseDialog() = runTest(testDispatcher) {
        val testGame = Game(
            id = 1,
            name = "Test Game",
            released = "2020-01-01",
            backgroundImage = "url",
            rating = 4.5,
            metacritic = 85,
            playtime = 10,
            genres = emptyList()
        )

        whenever(mockAddCustomGameUseCase(testGame)).thenReturn(Result.success(Unit))

        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.toggleAddGameDialog()
        assertTrue(viewModel.uiState.value.showAddGameDialog)

        viewModel.addCustomGame(testGame)
        testScheduler.advanceUntilIdle()

        verify(mockAddCustomGameUseCase)(testGame)
        assertFalse(viewModel.uiState.value.showAddGameDialog)
    }

    @Test
    fun toggleFavoriteShouldCallUseCase() = runTest(testDispatcher) {
        val testGame = Game(
            id = 2,
            name = "Favorite Game",
            released = "2021-05-15",
            backgroundImage = "url2",
            rating = 4.0,
            metacritic = 80,
            playtime = 5,
            genres = emptyList()
        )

        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.toggleFavorite(testGame)
        testScheduler.advanceUntilIdle()

        verify(mockToggleFavoritesUseCase)(testGame)
    }

    @Test
    fun loadFavoritesShouldUpdateStateWithFavoritesList() = runTest(testDispatcher) {
        val favoriteGames = listOf(
            Game(1, "Fav1", "2020-01-01", "url1", 4.5, 85, 10, emptyList()),
            Game(2, "Fav2", "2021-02-02", "url2", 4.0, 80, 5, emptyList())
        )

        whenever(mockGetFavoritesUseCase()).thenReturn(flowOf(favoriteGames))
        whenever(mockGetCustomGamesUseCase()).thenReturn(flowOf(emptyList()))

        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase
        )

        testScheduler.advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.favorites.size)
        assertEquals("Fav1", viewModel.uiState.value.favorites[0].name)
    }
}
