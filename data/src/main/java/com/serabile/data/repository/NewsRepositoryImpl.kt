package com.serabile.data.repository

import com.serabile.data.BuildConfig
import com.serabile.data.mapper.toDomain
import com.serabile.data.remote.api.NewsApiService
import com.serabile.data.util.LocaleProvider
import com.serabile.domain.model.Article
import com.serabile.domain.repository.NewsRepository
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val localeProvider: LocaleProvider,
) : NewsRepository {

    override suspend fun getTopHeadlines(): Result<List<Article>> = try {
        val country = localeProvider.getCountryCode()
        Timber.d("Fetching top headlines for country: $country")

        val response = newsApiService.getTopHeadlines(
            country = country,
            apiKey = BuildConfig.NEWS_API_KEY,
        )

        if (response.status == "ok") {
            Timber.d("API response OK - ${response.articles.size} articles received")
            Result.success(response.articles.toDomain())
        } else {
            Timber.w("API response KO, status: ${response.status}")
            Result.failure(Exception("API returned status: ${response.status}"))
        }
    } catch (e: Exception) {
        Timber.e(e, "Error fetching top headlines")
        Result.failure(e)
    }
}
