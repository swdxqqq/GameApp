package com.example.gameapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem (
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Games,
        icon = Icons.Default.Home,
        label = "Games"
    ),
    BottomNavItem(
        screen = Screen.Favorites,
        icon = Icons.Default.Favorite,
        label = "Favorites"
    )
)
