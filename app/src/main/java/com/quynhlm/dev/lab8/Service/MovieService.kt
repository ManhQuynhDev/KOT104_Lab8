package com.quynhlm.dev.lab8.Service

import retrofit2.Call
import com.quynhlm.dev.lab8.Model.Movie
import com.quynhlm.dev.lab8.Model.StatusResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MovieService {
    @GET("Movies")
    suspend fun getListMovies() : Response<List<Movie>>

//    @POST("Movies")
//    fun addMovie(@Body movie: Movie): Response<StatusResponse>

    @POST("Movies")
    suspend fun addMovie(@Body movie: Movie): Response<Void>

    @PUT("Movies/{id}")
    suspend fun updateMovie(@Path("id") id : String , @Body movie: Movie) : Response<Void>
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://65acc42dadbd5aa31bdf8563.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: MovieService by lazy {
        retrofit.create(MovieService::class.java)
    }
}