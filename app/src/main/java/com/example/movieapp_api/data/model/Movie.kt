    package com.example.movieapp_api.data.model

    import com.google.firebase.firestore.DocumentId

    data class Movie(
        @DocumentId
        var id: String? = null,
        var title: String = "",
        var year: String = "",
        var description: String = ""
    )