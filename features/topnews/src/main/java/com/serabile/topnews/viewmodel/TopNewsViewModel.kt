package com.serabile.topnews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serabile.domain.usecase.GetTopHeadlinesUseCase
import com.serabile.topnews.state.TopNewsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopNewsViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TopNewsUiState>(TopNewsUiState.Loading)
    val uiState: StateFlow<TopNewsUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    /**
     * Loads top headlines based on device locale (us, fr, ...)
     */
    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = TopNewsUiState.Loading

            getTopHeadlinesUseCase()
                .onSuccess { articles ->
                    _uiState.value = if (articles.isEmpty()) {
                        TopNewsUiState.Error("No news available")
                    } else {
                        TopNewsUiState.Success(articles)
                    }
                }
                .onFailure { exception ->
                    _uiState.value = TopNewsUiState.Error(
                        exception.message ?: "An error occurred"
                    )
                }
        }
    }
}
