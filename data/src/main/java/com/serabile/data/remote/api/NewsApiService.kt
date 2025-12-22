package com.serabile.data.remote.api

import com.serabile.data.remote.dto.NewsResponseData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Documentation here -> https://newsapi.org/docs/endpoints/top-headlines
 */
interface NewsApiService {

    /**
     * Get top headlines
     *
     * @param country Country code ("fr", "us")
     * @param apiKey The API key for authentication
     * @return List of articles
     */
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsResponseData

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
    }
}
