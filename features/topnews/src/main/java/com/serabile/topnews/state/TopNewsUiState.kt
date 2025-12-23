package com.serabile.topnews.state

import com.serabile.domain.model.Article

sealed class TopNewsUiState {

    data object Loading : TopNewsUiState()

    data class Success(
        val articles: List<Article>
    ) : TopNewsUiState()

    data class Error(
        val message: String
    ) : TopNewsUiState()
}
