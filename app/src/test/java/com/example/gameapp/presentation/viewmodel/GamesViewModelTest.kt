package com.example.gameapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.usecase.AddCustomGameUseCase
import com.example.gameapp.domain.usecase.GetCustomGamesUseCase
import com.example.gameapp.domain.usecase.GetFavoritesUseCase
import com.example.gameapp.domain.usecase.GetGamesUseCase
import com.example.gameapp.domain.usecase.ToggleFavoritesUseCase
import com.example.gameapp.domain.usecase.UploadImageUseCase
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
    private val mockUploadImageUseCase: UploadImageUseCase = mock()

    private lateinit var viewModel: GamesViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        runTest(testDispatcher) {
            whenever(mockGetGamesUseCase(page = 1, search = null, ordering = null)).thenReturn(emptyList())
        }
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
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
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
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
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
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
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
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
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
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
        )

        testScheduler.advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.favorites.size)
        assertEquals("Fav1", viewModel.uiState.value.favorites[0].name)
    }

    @Test
    fun loadCustomGamesShouldUpdateStateWithCustomGamesList() = runTest(testDispatcher) {
        val customGames = listOf(
            Game(100, "Custom1", "2023-01-01", "url100", 5.0, 0, 0, emptyList()),
            Game(101, "Custom2", "2023-02-02", "url101", 4.5, 0, 0, emptyList())
        )

        whenever(mockGetFavoritesUseCase()).thenReturn(flowOf(emptyList()))
        whenever(mockGetCustomGamesUseCase()).thenReturn(flowOf(customGames))

        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
        )

        testScheduler.advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.customGames.size)
        assertEquals("Custom1", viewModel.uiState.value.customGames[0].name)
    }

    @Test
    fun toggleFavoriteShouldHandleException() = runTest(testDispatcher) {
        val testGame = Game(3, "Error Game", "2020-01-01", "url3", 4.0, 80, 10, emptyList())
        val errorMessage = "Network error"

        whenever(mockToggleFavoritesUseCase(testGame)).thenThrow(RuntimeException(errorMessage))

        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.toggleFavorite(testGame)
        testScheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun addCustomGameShouldHandleFailure() = runTest(testDispatcher) {
        val testGame = Game(102, "Failed Game", "2023-03-03", "url102", 3.0, 0, 0, emptyList())
        val errorMessage = "Database error"

        whenever(mockAddCustomGameUseCase(testGame)).thenReturn(Result.failure(Exception(errorMessage)))

        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.toggleAddGameDialog()
        viewModel.addCustomGame(testGame)
        testScheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.error)
        assertTrue(viewModel.uiState.value.showAddGameDialog)
    }

    @Test
    fun initialStateShouldHaveCorrectDefaults() = runTest(testDispatcher) {
        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
        )

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
        assertEquals("", state.searchQuery)
    }

    @Test
    fun multipleTogglesShouldMaintainDialogState() = runTest(testDispatcher) {
        viewModel = GamesViewModel(
            mockGetGamesUseCase,
            mockGetFavoritesUseCase,
            mockToggleFavoritesUseCase,
            mockGetCustomGamesUseCase,
            mockAddCustomGameUseCase,
            mockUploadImageUseCase
        )

        testScheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showAddGameDialog)

        viewModel.toggleAddGameDialog()
        assertTrue(viewModel.uiState.value.showAddGameDialog)

        viewModel.toggleAddGameDialog()
        assertFalse(viewModel.uiState.value.showAddGameDialog)

        viewModel.toggleAddGameDialog()
        assertTrue(viewModel.uiState.value.showAddGameDialog)
    }
}
