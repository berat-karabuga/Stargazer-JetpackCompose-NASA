package com.stargazer.stargazer.data.remote

import com.stargazer.stargazer.data.model.ApodModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {

    @GET("planetary/apod")
    suspend fun getAstronomyPictureOfTheDay(
        @Query("api_key") apiKey: String
    ): Response<ApodModel>
}