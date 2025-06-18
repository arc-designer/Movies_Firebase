package com.example.movieapp_api.ui.movie

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movieapp_api.data.model.Movie
import java.util.UUID

@Composable
fun AddEditMovieScreen(
    navController: NavController,
    existingMovie: Movie? = null,
    viewModel: MovieViewModel,
    onDeleteMovie: ((Movie) -> Unit)? = null
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(existingMovie?.title ?: "") }
    var year by remember { mutableStateOf(existingMovie?.year ?: "") }
    var description by remember { mutableStateOf(existingMovie?.description ?: "") }
    var imageUrl by remember { mutableStateOf(existingMovie?.imageUrl ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = if (existingMovie == null) "Add Movie" else "Edit Movie",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val id = existingMovie?.id ?: UUID.randomUUID().toString()

                val movie = Movie(
                    id = id,
                    title = title,
                    year = year,
                    description = description,
                    imageUrl = imageUrl
                )

                if (existingMovie == null) {
                    viewModel.addMovie(movie) { success ->
                        if (success) {
                            Toast.makeText(context, "Movie added!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack("movieList", inclusive = false)
                        } else {
                            Toast.makeText(context, "Failed to add movie!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    viewModel.updateMovie(movie) { success ->
                        if (success) {
                            Toast.makeText(context, "Movie updated!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack("movieList", inclusive = false)
                        } else {
                            Toast.makeText(context, "Failed to update movie!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (existingMovie == null) "Add Movie" else "Update Movie")
        }

        if (existingMovie != null) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {
                    onDeleteMovie?.invoke(existingMovie)
                    Toast.makeText(context, "Movie deleted!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack("movieList", inclusive = false)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete Movie")
            }
        }
    }
}