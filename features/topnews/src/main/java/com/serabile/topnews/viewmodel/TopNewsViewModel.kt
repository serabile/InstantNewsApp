package com.serabile.topnews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serabile.domain.usecase.GetTopHeadlinesUseCase
import com.serabile.topnews.intent.TopNewsIntent
import com.serabile.topnews.state.TopNewsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopNewsViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TopNewsUiState>(TopNewsUiState.Loading)
    val uiState: StateFlow<TopNewsUiState> = _uiState.asStateFlow()

    init {
        processIntent(TopNewsIntent.LoadOrRefresh)
    }

    /**
     * User intents to update the UI state
     */
    fun processIntent(intent: TopNewsIntent) {
        Timber.d("Processing intent: ${intent::class.simpleName}")
        when (intent) {
            is TopNewsIntent.LoadOrRefresh -> loadNews()
            is TopNewsIntent.RetryLoad -> loadNews()
            is TopNewsIntent.ArticleClicked -> {
                // Navigation is handled in the UI layer
                // This intent is here for a potential future analytics
            }
        }
    }

    /**
     * Loads top headlines based on device locale (us, fr, ...)
     */
    private fun loadNews() {
        viewModelScope.launch {
            Timber.d("Loading top headlines...")
            _uiState.value = TopNewsUiState.Loading

            getTopHeadlinesUseCase()
                .onSuccess { articles ->
                    Timber.d("Successfully loaded ${articles.size} articles")
                    _uiState.value = if (articles.isEmpty()) {
                        TopNewsUiState.Error("No news available")
                    } else {
                        TopNewsUiState.Success(articles)
                    }
                }
                .onFailure { exception ->
                    Timber.e(exception, "Failed to load top headlines")
                    _uiState.value = TopNewsUiState.Error(
                        exception.message ?: "An error occurred",
                    )
                }
        }
    }
}
