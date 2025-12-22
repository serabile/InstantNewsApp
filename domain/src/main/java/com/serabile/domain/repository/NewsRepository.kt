package com.serabile.domain.repository

import com.serabile.domain.model.Article

interface NewsRepository {

    /**
     * Get top headlines for the country/language
     */
    suspend fun getTopHeadlines(language: String): Result<List<Article>>
}
