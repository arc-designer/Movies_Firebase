package com.example.movieapp_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp_api.ui.login.LoginScreen
import com.example.movieapp_api.ui.movie.AddEditMovieScreen
import com.example.movieapp_api.ui.movie.MovieListScreen
import com.example.movieapp_api.ui.movie.MovieViewModel
import com.example.movieapp_api.ui.register.RegisterScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            val viewModel: MovieViewModel = viewModel()

            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { navController.navigate("movieList") },
                            onNavigateToRegister = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { navController.navigate("movieList") },
                            onBackToLogin = { navController.popBackStack() }
                        )
                    }
                    composable("movieList") {
                        MovieListScreen(
                            viewModel = viewModel, // ✅ PASS THE SAME VIEWMODEL HERE
                            onAddMovieClick = { navController.navigate("addMovie") },
                            onEditMovieClick = { movieId ->
                                navController.navigate("editMovie/$movieId")
                            }
                        )
                    }
                    composable("addMovie") {
                        AddEditMovieScreen(
                            navController = navController,
                            viewModel = viewModel,
                            existingMovie = null
                        )
                    }
                    composable(
                        route = "editMovie/{movieId}",
                        arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")
                        val movieList by viewModel.movies.collectAsState()
                        val movie = remember(movieList, movieId) {
                            movieList.find { it.id == movieId }
                        }

                        movie?.let {
                            AddEditMovieScreen(
                                navController = navController,
                                viewModel = viewModel,
                                existingMovie = it,
                                onDeleteMovie = { movieToDelete ->
                                    viewModel.deleteMovie(movieToDelete.id!!) {
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}