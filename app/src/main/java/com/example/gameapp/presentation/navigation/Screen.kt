package com.example.gameapp.presentation.navigation

sealed class Screen(val route: String) {
    data object Games: Screen("games")
    data object Favorites: Screen("favorites")
}