package com.serabile.domain.repository

import com.serabile.domain.model.Article

interface NewsRepository {

    /**
     * Get top headlines for the user's device country/language
     */
    suspend fun getTopHeadlines(): Result<List<Article>>
}
