package com.serabile.topnews.viewmodel

import app.cash.turbine.test
import com.serabile.domain.model.Article
import com.serabile.domain.usecase.GetTopHeadlinesUseCase
import com.serabile.topnews.state.TopNewsUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopNewsViewModelTest {

    private lateinit var getTopHeadlinesUseCase: GetTopHeadlinesUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTopHeadlinesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Given
        coEvery { getTopHeadlinesUseCase() } returns Result.success(emptyList())

        // When
        val viewModel = TopNewsViewModel(getTopHeadlinesUseCase)

        // Then
        assertTrue(viewModel.uiState.value is TopNewsUiState.Loading)
    }

    @Test
    fun `loadNews emits Success state when use case returns articles`() = runTest {
        // Given
        val mockArticles = listOf(
            Article(
                id = "1",
                title = "Test Article",
                description = "Test Description",
                imageUrl = "https://test.com/image.jpg",
                url = "https://test.com",
                publishedAt = "2024-01-01T10:00:00Z",
                sourceName = "Test Source"
            )
        )
        coEvery { getTopHeadlinesUseCase() } returns Result.success(mockArticles)

        val viewModel = TopNewsViewModel(getTopHeadlinesUseCase)

        // When
        viewModel.uiState.test {
            // Skip Loading state
            awaitItem()
            advanceUntilIdle()
            
            // Then
            val state = awaitItem()
            assertTrue(state is TopNewsUiState.Success)
            assertEquals(mockArticles, (state as TopNewsUiState.Success).articles)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadNews emits Error state when use case returns failure`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getTopHeadlinesUseCase() } returns Result.failure(RuntimeException(errorMessage))

        val viewModel = TopNewsViewModel(getTopHeadlinesUseCase)

        // When
        viewModel.uiState.test {
            // Skip Loading state
            awaitItem()
            advanceUntilIdle()
            
            // Then
            val state = awaitItem()
            assertTrue(state is TopNewsUiState.Error)
            assertEquals(errorMessage, (state as TopNewsUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadNews reloads news and emits new state after error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val mockArticles = listOf(
            Article(
                id = "1",
                title = "Test Article",
                description = "Test Description",
                imageUrl = null,
                url = "https://test.com",
                publishedAt = "2024-01-01T10:00:00Z",
                sourceName = "Test Source"
            )
        )
        
        // First call fails, second succeeds
        coEvery { getTopHeadlinesUseCase() } returns Result.failure(RuntimeException(errorMessage)) andThen Result.success(mockArticles)

        val viewModel = TopNewsViewModel(getTopHeadlinesUseCase)

        viewModel.uiState.test {
            // Skip Loading state
            awaitItem()
            advanceUntilIdle()
            
            // Error state
            assertTrue(awaitItem() is TopNewsUiState.Error)
            
            // Reload
            viewModel.loadNews()
            
            // Loading state after reload
            assertTrue(awaitItem() is TopNewsUiState.Loading)
            advanceUntilIdle()
            
            // Success state
            val state = awaitItem()
            assertTrue(state is TopNewsUiState.Success)
            assertEquals(mockArticles, (state as TopNewsUiState.Success).articles)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
