package com.serabile.topnews.intent

import com.serabile.domain.model.Article

/**
 * User actions
 */
sealed class TopNewsIntent {
    data object LoadOrRefresh : TopNewsIntent()

    // User wants to retry loading after an error
    data object RetryLoad : TopNewsIntent()

    data class ArticleClicked(val article: Article) : TopNewsIntent()
}
