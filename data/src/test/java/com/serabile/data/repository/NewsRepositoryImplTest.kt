package com.serabile.data.repository

import com.serabile.data.remote.api.NewsApiService
import com.serabile.data.remote.dto.ArticleData
import com.serabile.data.remote.dto.NewsResponseData
import com.serabile.data.remote.dto.SourceData
import com.serabile.data.util.LocaleProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {

    private lateinit var newsApiService: NewsApiService
    private lateinit var localeProvider: LocaleProvider
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        newsApiService = mockk()
        localeProvider = mockk()
        repository = NewsRepositoryImpl(newsApiService, localeProvider)
    }

    @Test
    fun `getTopHeadlines returns success with mapped articles`() = runTest {
        // Given
        val mockArticleData = ArticleData(
            source = SourceData(id = "1", name = "Test Source"),
            author = "Author",
            title = "Test Title",
            description = "Test Description",
            url = "https://test.com",
            urlToImage = "https://test.com/image.jpg",
            publishedAt = "2024-01-01T10:00:00Z",
            content = "Test Content"
        )
        val mockResponse = NewsResponseData(
            status = "ok",
            totalResults = 1,
            articles = listOf(mockArticleData)
        )

        coEvery { localeProvider.getCountryCode() } returns "us"
        coEvery { newsApiService.getTopHeadlines(any(), any()) } returns mockResponse

        // When
        val result = repository.getTopHeadlines()

        // Then
        assertTrue(result.isSuccess)
        val articles = result.getOrNull()
        assertEquals(1, articles?.size)
        assertEquals("Test Title", articles?.first()?.title)
        assertEquals("Test Description", articles?.first()?.description)
        assertEquals("Test Source", articles?.first()?.sourceName)
    }

    @Test
    fun `getTopHeadlines returns failure on exception`() = runTest {
        // Given
        coEvery { localeProvider.getCountryCode() } returns "us"
        coEvery {
            newsApiService.getTopHeadlines(
                any(),
                any()
            )
        } throws RuntimeException("Network error")

        // When
        val result = repository.getTopHeadlines()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
