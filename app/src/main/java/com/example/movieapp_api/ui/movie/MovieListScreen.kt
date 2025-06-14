package com.example.movieapp_api.ui.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp_api.data.model.Movie

@Composable
fun MovieListScreen(
    viewModel: MovieViewModel = viewModel(),
    onAddMovieClick: () -> Unit,
    onEditMovieClick: (String) -> Unit
) {
    val movies by viewModel.movies.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Movie List",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (movies.isEmpty()) {
                Text("No movies available.")
            } else {
                LazyColumn {
                    items(movies) { movie ->
                        MovieItem(movie = movie, onClick = { movie.id?.let { onEditMovieClick(it) } })
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddMovieClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Movie")
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Title: ${movie.title}", style = MaterialTheme.typography.titleMedium)
            Text("Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
            Text("Description: ${movie.description}", style = MaterialTheme.typography.bodySmall)
        }
    }
}