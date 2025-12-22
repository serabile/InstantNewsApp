package com.serabile.data.repository

import com.serabile.data.BuildConfig
import com.serabile.data.mapper.toDomain
import com.serabile.data.remote.api.NewsApiService
import com.serabile.domain.model.Article
import com.serabile.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(language: String): Result<List<Article>> {
        return try {
            val response = newsApiService.getTopHeadlines(
                country = language,
                apiKey = BuildConfig.NEWS_API_KEY
            )

            if (response.status == "ok") {
                Result.success(response.articles.toDomain())
            } else {
                Result.failure(Exception("API returned status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
