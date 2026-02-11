package com.stargazer.stargazer.domain.repository

import com.stargazer.stargazer.data.model.ApodModel
import com.stargazer.stargazer.util.Resource

interface NasaRepository {
    suspend fun getDailyPhoto(): Resource<ApodModel>
}