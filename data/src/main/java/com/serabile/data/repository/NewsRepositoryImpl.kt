package com.serabile.data.repository

import com.serabile.data.BuildConfig
import com.serabile.data.mapper.toDomain
import com.serabile.data.remote.api.NewsApiService
import com.serabile.data.util.LocaleProvider
import com.serabile.domain.model.Article
import com.serabile.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val localeProvider: LocaleProvider
) : NewsRepository {

    override suspend fun getTopHeadlines(): Result<List<Article>> {
        return try {
            val country = localeProvider.getCountryCode()
            val response = newsApiService.getTopHeadlines(
                country = country,
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
