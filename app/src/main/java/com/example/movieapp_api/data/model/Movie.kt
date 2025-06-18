package com.example.movieapp_api.data.model

data class Movie(
    var id: String? = null,
    var title: String = "",
    var year: String = "",
    var description: String = "",
    var imageUrl: String = ""
)