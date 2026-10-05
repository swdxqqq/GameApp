package com.example.gameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.gameapp.presentation.navigation.BottomNavigationBar
import com.example.gameapp.presentation.navigation.NavGraph
import com.example.gameapp.presentation.screen.GamesScreen
import com.example.gameapp.presentation.viewmodel.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.gameapp.presentation.theme.GameAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameAppTheme {
                val navController = rememberNavController()
                val viewModel = hiltViewModel<GamesViewModel>()

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ){ paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ){
                        NavGraph(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}