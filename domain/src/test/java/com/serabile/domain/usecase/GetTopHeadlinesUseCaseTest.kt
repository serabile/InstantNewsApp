package com.serabile.domain.usecase

import com.serabile.domain.model.Article
import com.serabile.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTopHeadlinesUseCaseTest {

    private lateinit var repository: NewsRepository
    private lateinit var useCase: GetTopHeadlinesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTopHeadlinesUseCase(repository)
    }

    @Test
    fun `invoke calls repository`() = runTest {
        // Given
        coEvery { repository.getTopHeadlines() } returns Result.success(emptyList())

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.getTopHeadlines() }
    }

    @Test
    fun `invoke returns success with articles from repository`() = runTest {
        // Given
        val mockArticles = listOf(
            Article(
                id = "1",
                title = "Test Article",
                description = "Test Description",
                imageUrl = "https://test.com/image.jpg",
                url = "https://test.com",
                publishedAt = "2024-01-01T10:00:00Z",
                sourceName = "Test Source",
            ),
        )
        coEvery { repository.getTopHeadlines() } returns Result.success(mockArticles)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Test Article", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getTopHeadlines() } returns Result.failure(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
