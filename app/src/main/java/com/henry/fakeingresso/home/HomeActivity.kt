package com.henry.fakeingresso.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.henry.fakeingresso.bottomnavigation.BottomNavItem
import com.henry.fakeingresso.bottomnavigation.BottomNavigationBar
import com.henry.fakeingresso.detail.DetailScreen
import com.henry.fakeingresso.home.components.HomeScreen
import com.henry.fakeingresso.home.viewmodel.HomeViewModel
import com.henry.fakeingresso.ui.theme.DarkBackground
import com.henry.fakeingresso.ui.theme.FakeIngressoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseHomeActivity() {

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.refreshMovies(isConnected())
        setContent {
            FakeIngressoTheme {
                val navController = rememberNavController()
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    containerColor = DarkBackground,
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(BottomNavItem.Home.route) {
                            HomeScreen(
                                uiState = uiState,
                                onRetry = { viewModel.refreshMovies(isConnected()) },
                                onMovieClick = { },
                                onSearchQueryChanged = viewModel::onSearchQueryChanged
                            )
                        }
                        composable(BottomNavItem.Detail.route) {
                            DetailScreen()
                        }
                    }
                }
            }
        }
    }
}
