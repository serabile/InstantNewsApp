package com.serabile.topnews.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.serabile.designsystem.component.ErrorScreen
import com.serabile.designsystem.component.LoadingScreen
import com.serabile.designsystem.component.NewsCard
import com.serabile.designsystem.theme.InstantNewsTheme
import com.serabile.domain.model.Article
import com.serabile.topnews.intent.TopNewsIntent
import com.serabile.topnews.state.TopNewsUiState
import com.serabile.topnews.viewmodel.TopNewsViewModel

/**
 * Top News screen displaying a list of news articles
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNewsScreen(
    viewModel: TopNewsViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = com.serabile.designsystem.R.drawable.ic_launcher),
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(com.serabile.topnews.R.string.top_news_title),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                },

                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { viewModel.processIntent(TopNewsIntent.LoadOrRefresh) }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(com.serabile.topnews.R.string.refresh),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        TopNewsContent(
            uiState = uiState,
            onArticleClick = onArticleClick,
            onRetry = { viewModel.processIntent(TopNewsIntent.RetryLoad) },
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
fun TopNewsContent(
    uiState: TopNewsUiState,
    onArticleClick: (Article) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is TopNewsUiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is TopNewsUiState.Success -> {
            ArticlesList(
                articles = uiState.articles,
                onArticleClick = onArticleClick,
                modifier = modifier,
            )
        }

        is TopNewsUiState.Error -> {
            ErrorScreen(
                message = uiState.message,
                onRetry = onRetry,
                modifier = modifier,
            )
        }
    }
}

@Composable
fun ArticlesList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = articles,
            key = { it.id },
        ) { article ->
            NewsCard(
                title = article.title,
                imageUrl = article.imageUrl,
                onClick = { onArticleClick(article) },
            )
        }
    }
}

// ==================== PREVIEWS ====================

private val previewArticles = listOf(
    Article(
        id = "1",
        title = "Breaking News: Major Technology Breakthrough Announced Today",
        description = "Scientists have made a groundbreaking discovery that could change the world.",
        imageUrl = null,
        url = "https://example.com/article1",
        publishedAt = "2025-12-19T10:00:00Z",
        sourceName = "Tech News",
    ),
    Article(
        id = "2",
        title = "Sports: Championship Finals Set for This Weekend",
        description = "The biggest teams will face off in what promises to be an exciting match.",
        imageUrl = null,
        url = "https://example.com/article2",
        publishedAt = "2025-12-19T09:30:00Z",
        sourceName = "Sports Daily",
    ),
    Article(
        id = "3",
        title = "Weather Alert: Storm System Moving Through the Region",
        description = "Residents are advised to prepare for severe weather conditions.",
        imageUrl = null,
        url = "https://example.com/article3",
        publishedAt = "2025-12-19T08:15:00Z",
        sourceName = "Weather Channel",
    ),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopNewsContentLoadingPreview() {
    InstantNewsTheme {
        TopNewsContent(
            uiState = TopNewsUiState.Loading,
            onArticleClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopNewsContentSuccessPreview() {
    InstantNewsTheme {
        TopNewsContent(
            uiState = TopNewsUiState.Success(previewArticles),
            onArticleClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopNewsContentErrorPreview() {
    InstantNewsTheme {
        TopNewsContent(
            uiState = TopNewsUiState.Error("Une erreur est survenue. Vérifiez votre connexion internet."),
            onArticleClick = {},
            onRetry = {},
        )
    }
}
