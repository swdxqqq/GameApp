package com.example.gameapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gameapp.presentation.screen.FavoritesScreen
import com.example.gameapp.presentation.screen.GamesScreen
import com.example.gameapp.presentation.viewmodel.GamesViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: GamesViewModel
){
    NavHost(
        navController = navController,
        startDestination = Screen.Games.route
    ){
        composable(Screen.Games.route){
            GamesScreen(viewModel = viewModel)
        }
        composable(Screen.Favorites.route){
            FavoritesScreen(viewModel = viewModel)
        }
    }
}