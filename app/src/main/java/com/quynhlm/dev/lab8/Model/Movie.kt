package com.quynhlm.dev.lab8.Model

import com.google.gson.annotations.SerializedName


data class Movie(
    @SerializedName("filmid") val filmid: String?,
    @SerializedName("filmname") var filmname: String,
    @SerializedName("duration") var duration: String,
    @SerializedName("releaseDate") var releaseDate: String,
    @SerializedName("genre") var genre: String,
    @SerializedName("national") var national: String,
    @SerializedName("description") var description: String,
    @SerializedName("image") var image: String,
)