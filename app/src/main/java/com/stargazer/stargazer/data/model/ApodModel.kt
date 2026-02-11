package com.stargazer.stargazer.data.model

import com.google.gson.annotations.SerializedName

data class ApodModel(
    @SerializedName("title")
    val title: String?,

    @SerializedName("explanation")
    val explanation: String?,

    @SerializedName("date")
    val date: String?,

    @SerializedName("url")
    val imageUrl: String?,

    @SerializedName("hdurl")
    val hdImageUrl: String?,

    @SerializedName("media_type")
    val mediaType: String?,

    @SerializedName("copyright")
    val copyright: String?
)