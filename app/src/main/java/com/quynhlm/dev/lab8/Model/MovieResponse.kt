package com.quynhlm.dev.lab8.Model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("filmId") val filmId: String?,
    @SerializedName("filmName") val filmName: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("national") val national: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
)