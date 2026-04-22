package com.henry.fakeingresso.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.henry.fakeingresso.bottomnavigation.BottomNavItem
import com.henry.fakeingresso.bottomnavigation.BottomNavigationBar
import com.henry.fakeingresso.detail.DetailScreen
import com.henry.fakeingresso.detail.DetailViewModel
import com.henry.fakeingresso.favorites.FavoritesScreen
import com.henry.fakeingresso.favorites.FavoritesViewModel
import com.henry.fakeingresso.home.components.HomeScreen
import com.henry.fakeingresso.home.viewmodel.HomeViewModel
import com.henry.fakeingresso.ui.theme.DarkBackground
import com.henry.fakeingresso.ui.theme.FakeIngressoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseHomeActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val detailViewModel: DetailViewModel by viewModel()
    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        homeViewModel.refreshMovies(isConnected())
        setContent {
            FakeIngressoTheme {
                val navController = rememberNavController()
                val uiState by homeViewModel.uiState.collectAsState()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBottomBar = currentRoute != DETAIL_ROUTE

                Scaffold(
                    containerColor = DarkBackground,
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(navController)
                        }
                    }
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
                                onRetry = { homeViewModel.refreshMovies(isConnected()) },
                                onMovieClick = { movie ->
                                    navController.navigate("detail/${movie.id}")
                                },
                                onSearchQueryChanged = homeViewModel::onSearchQueryChanged
                            )
                        }
                        composable(BottomNavItem.Favorites.route) {
                            val favoriteMovies by favoritesViewModel.favoriteMovies.collectAsState()

                            FavoritesScreen(
                                movies = favoriteMovies,
                                onMovieClick = { movie ->
                                    navController.navigate("detail/${movie.id}")
                                }
                            )
                        }
                        composable(
                            route = DETAIL_ROUTE,
                            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("movieId") ?: return@composable
                            val movie by detailViewModel.movie.collectAsState()
                            val isFavorite by detailViewModel.isFavorite.collectAsState()

                            LaunchedEffect(movieId) {
                                detailViewModel.loadMovie(movieId)
                            }

                            DetailScreen(
                                movie = movie,
                                isFavorite = isFavorite,
                                onBackClick = { navController.popBackStack() },
                                onFavoriteClick = detailViewModel::toggleFavorite
                            )
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val DETAIL_ROUTE = "detail/{movieId}"
    }
}
