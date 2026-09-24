package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.repository.MovieRepository
import com.example.ui.components.BottomTab
import com.example.ui.components.CastDialog
import com.example.ui.components.CineNovaBottomBar
import com.example.ui.components.CineNovaTopBar
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.screens.CinemaPlayerScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.FilmDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MovieDetailScreen
import com.example.ui.screens.MySpaceScreen
import com.example.ui.screens.ReviewsScreen
import com.example.ui.screens.SearchCurationScreen
import com.example.ui.screens.StudioOpsScreen
import com.example.ui.screens.SubscriptionScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MovieRepository.initRoomDatabases(applicationContext)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CineNovaApp()
            }
        }
    }
}

@Composable
fun CineNovaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomTab.HOME.route

    var showLanguagePicker by remember { mutableStateOf(false) }
    var showCastDialog by remember { mutableStateOf(false) }

    val isTopLevelRoute = currentRoute in listOf(
        BottomTab.HOME.route,
        BottomTab.DISCOVER.route,
        BottomTab.SEARCH.route,
        BottomTab.DOWNLOADS.route,
        BottomTab.MY_SPACE.route
    )

    val isPlayerRoute = currentRoute.startsWith("player/")
    val isSubscriptionRoute = currentRoute == "subscription"
    val isStudioOpsRoute = currentRoute == "studio_ops"

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        topBar = {
            if (!isPlayerRoute && !isSubscriptionRoute && !isStudioOpsRoute) {
                val isDetail = currentRoute.startsWith("detail/") || currentRoute.startsWith("film_detail/")
                val isWatchlist = currentRoute == "watchlist"
                val isReviews = currentRoute.startsWith("reviews")
                CineNovaTopBar(
                    title = if (isWatchlist) "My Watchlist" else if (isReviews) "Indie Reviews" else if (isDetail) "Film Details" else null,
                    showBack = !isTopLevelRoute,
                    onBack = { navController.popBackStack() },
                    onLanguageClick = { showLanguagePicker = true },
                    onCastClick = { showCastDialog = true },
                    onProfileClick = {
                        if (currentRoute != BottomTab.MY_SPACE.route) {
                            navController.navigate(BottomTab.MY_SPACE.route)
                        }
                    },
                    onStudioOpsClick = {
                        navController.navigate("studio_ops")
                    },
                    onWatchlistClick = {
                        if (currentRoute != "watchlist") {
                            navController.navigate("watchlist")
                        }
                    },
                    onReviewsClick = {
                        if (!currentRoute.startsWith("reviews")) {
                            navController.navigate("reviews")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isTopLevelRoute) {
                CineNovaBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { tab ->
                        if (currentRoute != tab.route) {
                            navController.navigate(tab.route) {
                                popUpTo(BottomTab.HOME.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = BottomTab.HOME.route
            ) {
                composable(BottomTab.HOME.route) {
                    HomeScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate("detail/$movieId")
                        },
                        onNavigateToPlayer = { movieId ->
                            navController.navigate("player/$movieId")
                        },
                        onNavigateToSearchWithQuery = { query ->
                            MovieRepository.setSearchQuery(query)
                            navController.navigate(BottomTab.SEARCH.route)
                        }
                    )
                }

                composable(BottomTab.DISCOVER.route) {
                    DiscoverScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate("film_detail/$movieId")
                        },
                        onNavigateToPlayer = { movieId ->
                            navController.navigate("player/$movieId")
                        }
                    )
                }

                composable(BottomTab.SEARCH.route) {
                    SearchCurationScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate("film_detail/$movieId")
                        },
                        onNavigateToPlayer = { movieId ->
                            navController.navigate("player/$movieId")
                        }
                    )
                }

                composable(BottomTab.DOWNLOADS.route) {
                    DownloadsScreen(
                        onNavigateToPlayer = { movieId ->
                            navController.navigate("player/$movieId")
                        },
                        onNavigateToDiscover = {
                            navController.navigate(BottomTab.DISCOVER.route)
                        }
                    )
                }

                composable(BottomTab.MY_SPACE.route) {
                    MySpaceScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate("film_detail/$movieId")
                        },
                        onNavigateToPlayer = { movieId ->
                            navController.navigate("player/$movieId")
                        },
                        onNavigateToDownloads = {
                            navController.navigate(BottomTab.DOWNLOADS.route)
                        },
                        onNavigateToSubscription = {
                            navController.navigate("subscription")
                        },
                        onNavigateToWatchlist = {
                            navController.navigate("watchlist")
                        },
                        onNavigateToReviews = {
                            navController.navigate("reviews")
                        }
                    )
                }

                composable("watchlist") {
                    WatchlistScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate("film_detail/$movieId")
                        },
                        onNavigateToPlayer = { movieId ->
                            navController.navigate("player/$movieId")
                        },
                        onNavigateToDiscover = {
                            navController.navigate(BottomTab.DISCOVER.route)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("reviews") {
                    ReviewsScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate("film_detail/$movieId")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "reviews/{filmId}",
                    arguments = listOf(navArgument("filmId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val filmId = backStackEntry.arguments?.getString("filmId")
                    ReviewsScreen(
                        initialFilmId = filmId,
                        onNavigateToDetail = { movieId ->
                            navController.navigate("film_detail/$movieId")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "film_detail/{movieId}",
                    arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: "all_we_imagine_as_light"
                    FilmDetailScreen(
                        movieId = movieId,
                        onBack = { navController.popBackStack() },
                        onPlayMovie = { id ->
                            navController.navigate("player/$id")
                        },
                        onNavigateToReviews = { id ->
                            navController.navigate("reviews/$id")
                        }
                    )
                }

                composable(
                    route = "detail/{movieId}",
                    arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: "manjummel_boys"
                    FilmDetailScreen(
                        movieId = movieId,
                        onBack = { navController.popBackStack() },
                        onPlayMovie = { id ->
                            navController.navigate("player/$id")
                        },
                        onNavigateToReviews = { id ->
                            navController.navigate("reviews/$id")
                        }
                    )
                }

                composable(
                    route = "player/{movieId}",
                    arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: "manjummel_boys"
                    CinemaPlayerScreen(
                        movieId = movieId,
                        onBack = { navController.popBackStack() },
                        onPlayMovie = { id ->
                            navController.navigate("player/$id") {
                                popUpTo("player/$movieId") { inclusive = true }
                            }
                        }
                    )
                }

                composable("subscription") {
                    SubscriptionScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("studio_ops") {
                    StudioOpsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    if (showLanguagePicker) {
        LanguagePickerDialog(
            selectedLanguage = MovieRepository.selectedLanguage.value,
            onLanguageSelected = { lang ->
                MovieRepository.setLanguageFilter(lang)
            },
            onDismiss = { showLanguagePicker = false }
        )
    }

    if (showCastDialog) {
        CastDialog(onDismiss = { showCastDialog = false })
    }
}
