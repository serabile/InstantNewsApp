package com.serabile.topnews.screen

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.serabile.designsystem.theme.InstantNewsTheme
import com.serabile.domain.model.Article
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockArticle = Article(
        id = "1",
        title = "Breaking News: Technology Breakthrough Announced",
        description = "Scientists have made a groundbreaking discovery that could change the world. " +
            "This is a longer description to test text overflow and scrolling behavior in the detail screen.",
        imageUrl = "https://example.com/image1.jpg",
        url = "https://example.com/article1",
        publishedAt = "2025-12-19T10:00:00Z",
        sourceName = "Tech News",
    )

    @Test
    fun newsDetailScreen_displaysArticleInformation() {
        composeTestRule.setContent {
            InstantNewsTheme {
                NewsDetailScreen(
                    article = mockArticle,
                    onBackClick = {},
                )
            }
        }

        // Verify source name is displayed in TopAppBar
        composeTestRule
            .onNodeWithText(mockArticle.sourceName)
            .assertExists()

        // Verify article title is displayed
        composeTestRule
            .onNodeWithText(mockArticle.title)
            .assertExists()

        // Verify article description is displayed
        composeTestRule
            .onNodeWithText(mockArticle.description.orEmpty())
            .assertExists()
    }

    @Test
    fun newsDetailScreen_displayedImage() {
        composeTestRule.setContent {
            InstantNewsTheme {
                NewsDetailScreen(
                    article = mockArticle,
                    onBackClick = {},
                )
            }
        }

        // Verify NewsImage component is present
        composeTestRule
            .onNodeWithTag("article_image")
            .assertExists()
    }

    @Test
    fun newsDetailScreen_backButton_callsCallback() {
        var backClicked = false

        composeTestRule.setContent {
            InstantNewsTheme {
                NewsDetailScreen(
                    article = mockArticle,
                    onBackClick = { backClicked = true },
                )
            }
        }

        // Click back button
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        // Verify callback was called
        assert(backClicked)
    }

    @Test
    fun newsDetailScreen_openInBrowserButton_displayed() {
        composeTestRule.setContent {
            InstantNewsTheme {
                NewsDetailScreen(
                    article = mockArticle,
                    onBackClick = {},
                )
            }
        }

        // Verify "Open article" button is displayed and clickable
        composeTestRule
            .onNodeWithTag("read_article_button")
            .assert(hasClickAction())
            .assertExists()
    }

    @Test
    fun newsDetailScreen_articleWithoutImage() {
        val articleWithoutImage = mockArticle.copy(imageUrl = null)

        composeTestRule.setContent {
            InstantNewsTheme {
                NewsDetailScreen(
                    article = articleWithoutImage,
                    onBackClick = {},
                )
            }
        }

        // Verify screen still displays properly
        composeTestRule
            .onNodeWithText(articleWithoutImage.title)
            .assertExists()

        composeTestRule
            .onNodeWithText(articleWithoutImage.description.orEmpty())
            .assertExists()
    }

    @Test
    fun newsDetailScreen_longTitle_displaysProperly() {
        val articleWithLongTitle = mockArticle.copy(
            title = "This is a very long title that might wrap to multiple lines " +
                "and should be handled gracefully by the composable layout"
        )

        composeTestRule.setContent {
            InstantNewsTheme {
                NewsDetailScreen(
                    article = articleWithLongTitle,
                    onBackClick = {},
                )
            }
        }

        // Verify long title is displayed
        composeTestRule
            .onNodeWithText(articleWithLongTitle.title)
            .assertExists()
    }
}
