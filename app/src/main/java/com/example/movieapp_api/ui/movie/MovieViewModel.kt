package com.example.movieapp_api.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp_api.data.model.Movie
import com.example.movieapp_api.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    init {
        observeMovies()
    }

    private fun observeMovies() {
        viewModelScope.launch {
            repository.getMoviesRealtime().collectLatest { movieList ->
                _movies.value = movieList
            }
        }
    }

    fun addMovie(movie: Movie, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.addMovie(movie)
            if (success) observeMovies()
            onComplete(success)
        }
    }

    fun updateMovie(movie: Movie, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.updateMovie(movie)
            if (success) observeMovies()
            onComplete(success)
        }
    }

    fun deleteMovie(movieId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.deleteMovie(movieId)
            if (success) observeMovies()
            onComplete(success)
        }
    }
}