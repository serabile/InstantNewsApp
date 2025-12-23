package com.serabile.topnews.screen

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.serabile.designsystem.theme.InstantNewsTheme
import com.serabile.domain.model.Article
import com.serabile.topnews.state.TopNewsUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopNewsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockArticles = listOf(
        Article(
            id = "1",
            title = "Breaking News: Technology Breakthrough",
            description = "Scientists have made a groundbreaking discovery.",
            imageUrl = "https://example.com/image1.jpg",
            url = "https://example.com/article1",
            publishedAt = "2025-12-19T10:00:00Z",
            sourceName = "Tech News",
        ),
        Article(
            id = "2",
            title = "Sports: Championship Finals",
            description = "The biggest teams will face off this weekend.",
            imageUrl = "https://example.com/image2.jpg",
            url = "https://example.com/article2",
            publishedAt = "2025-12-19T09:30:00Z",
            sourceName = "Sports Daily",
        ),
    )

    @Test
    fun topNewsContent_displayLoadingState() {
        composeTestRule.setContent {
            InstantNewsTheme {
                TopNewsContent(
                    uiState = TopNewsUiState.Loading,
                    onArticleClick = {},
                    onRetry = {},
                )
            }
        }

        // Verify loading screen is displayed
        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertExists()
    }

    @Test
    fun topNewsContent_displaySuccessState_withArticles() {
        composeTestRule.setContent {
            InstantNewsTheme {
                TopNewsContent(
                    uiState = TopNewsUiState.Success(mockArticles),
                    onArticleClick = {},
                    onRetry = {},
                )
            }
        }

        // Verify articles are displayed
        composeTestRule
            .onNodeWithText(mockArticles[0].title)
            .assertExists()

        composeTestRule
            .onNodeWithText(mockArticles[1].title)
            .assertExists()
    }

    @Test
    fun topNewsContent_displayErrorState() {
        val errorMessage = "Une erreur est survenue. Vérifiez votre connexion internet."

        composeTestRule.setContent {
            InstantNewsTheme {
                TopNewsContent(
                    uiState = TopNewsUiState.Error(errorMessage),
                    onArticleClick = {},
                    onRetry = {},
                )
            }
        }

        // Verify error message is displayed
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()

        // Verify retry button is displayed
        composeTestRule
            .onNodeWithTag("retry_button")
            .assert(hasClickAction())
    }

    @Test
    fun articlesList_clickArticle_callsCallback() {
        var clickedArticle: Article? = null

        composeTestRule.setContent {
            InstantNewsTheme {
                ArticlesList(
                    articles = mockArticles,
                    onArticleClick = { clickedArticle = it },
                )
            }
        }

        // Click on the first article
        composeTestRule
            .onNodeWithText(mockArticles[0].title)
            .performClick()

        // Verify callback was called with correct article
        assert(clickedArticle == mockArticles[0])
    }

    @Test
    fun articlesList_displaysAllArticles() {
        composeTestRule.setContent {
            InstantNewsTheme {
                ArticlesList(
                    articles = mockArticles,
                    onArticleClick = {},
                )
            }
        }

        // Verify all articles are displayed
        mockArticles.forEach { article ->
            composeTestRule
                .onNodeWithText(article.title)
                .assertExists()
        }
    }

    @Test
    fun articlesList_emptyList_displaysNothing() {
        composeTestRule.setContent {
            InstantNewsTheme {
                ArticlesList(
                    articles = emptyList(),
                    onArticleClick = {},
                )
            }
        }

        // Verify no articles are displayed
        composeTestRule
            .onAllNodesWithTag("news_card")
            .assertCountEquals(0)
    }

    @Test
    fun topNewsContent_errorState_retryButton_callsCallback() {
        var retryClicked = false

        composeTestRule.setContent {
            InstantNewsTheme {
                TopNewsContent(
                    uiState = TopNewsUiState.Error("Error message"),
                    onArticleClick = {},
                    onRetry = { retryClicked = true },
                )
            }
        }

        // Click retry button
        composeTestRule
            .onNodeWithTag("retry_button")
            .performClick()

        // Verify callback was called
        assert(retryClicked)
    }
}
