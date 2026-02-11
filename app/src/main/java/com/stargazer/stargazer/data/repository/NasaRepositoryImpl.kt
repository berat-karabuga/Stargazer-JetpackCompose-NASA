package com.stargazer.stargazer.data.repository

import com.stargazer.stargazer.data.remote.NasaApi
import com.stargazer.stargazer.data.model.ApodModel
import com.stargazer.stargazer.domain.repository.NasaRepository
import com.stargazer.stargazer.util.Resource
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    private val api: NasaApi
) : NasaRepository {

    override suspend fun getDailyPhoto(): Resource<ApodModel> {
        return try {

            val response = api.getAstronomyPictureOfTheDay("TkHbW52i2nhr3ZRfFGAdFu6hJ84YHa2nrK9QmZjq")

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Bir hata oluştu")
            }
        } catch (e: Exception) {
            Resource.Error("İnternet bağlantını kontrol et: ${e.localizedMessage}")
        }
    }
}