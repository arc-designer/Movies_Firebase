package com.example.movieapp_api.repository

import com.example.movieapp_api.data.model.Movie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MovieRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getMoviesRealtime(): Flow<List<Movie>> = callbackFlow {
        val listener = db.collection("movies")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val movies = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Movie::class.java)
                } ?: emptyList()

                trySend(movies).isSuccess
            }

        awaitClose { listener.remove() }
    }

    suspend fun addMovie(movie: Movie): Boolean {
        return try {
            db.collection("movies")
                .add(movie.copy(id = null)) // remove id before writing
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateMovie(movie: Movie): Boolean {
        return try {
            val movieId = movie.id ?: return false
            db.collection("movies")
                .document(movieId)
                .set(movie.copy(id = null)) // remove id before updating
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteMovie(movieId: String): Boolean {
        return try {
            db.collection("movies")
                .document(movieId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}