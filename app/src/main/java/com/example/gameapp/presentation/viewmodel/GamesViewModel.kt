package com.example.gameapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameapp.domain.model.Game
import com.example.gameapp.domain.usecase.GetFavoritesUseCase
import com.example.gameapp.domain.usecase.GetGamesUseCase
import com.example.gameapp.domain.usecase.ToggleFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Класс для хранения состояния экрана
data class GamesUiState(
    val games: List<Game> = emptyList(),
    val favorites: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1
)

class GamesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoritesUseCase: ToggleFavoritesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(GamesUiState()) // Приватное изменяемое свойство доступное только из ViewModel
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow() // Свойство, которое подписывается на оригинал. Можно только читать

    init{
        loadGames()
        loadFavorites()
    }

    // Корутина для загрузки игр
    private fun loadGames(page: Int = 1){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try{
                val games = getGamesUseCase(page = page)
                _uiState.value = _uiState.value.copy(
                    games = games,
                    currentPage = page,
                    isLoading = false
                )
            }
            catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    // Корутина для сбора избранных игр
    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { // collect - потому что подписываемся на получаемые данные
                favoritesList ->
                _uiState.value = _uiState.value.copy(favorites = favoritesList)
            }
        }
    }

    // Корутина для добавления в избранное
    fun toggleFavorite(game: Game){
        viewModelScope.launch {
            try{
                toggleFavoritesUseCase(game)
            }
            catch (e: Exception){
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    // Функция для вызова корутины с новой страницей
    fun loadNextPage(){
        val nextPage = _uiState.value.currentPage + 1
        loadGames(nextPage)
    }
}