package com.serabile.topnews.screen

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.serabile.designsystem.component.NewsImage
import com.serabile.designsystem.theme.InstantNewsTheme
import com.serabile.domain.model.Article
import com.serabile.topnews.R

/**
 * News detail screen displaying full article information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    article: Article,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = article.sourceName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        NewsDetailContent(
            article = article,
            onOpenInBrowser = {
                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                context.startActivity(intent)
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun NewsDetailContent(
    article: Article,
    onOpenInBrowser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Article Image
        NewsImage(
            imageUrl = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Source and date
            Text(
                text = "${article.sourceName} • ${formatDate(article.publishedAt)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            article.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Read more button
            Button(
                onClick = onOpenInBrowser,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.OpenInBrowser,
                    contentDescription = stringResource(R.string.read_full_article),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(stringResource(R.string.read_full_article))
            }
        }
    }
}

/**
 * Formats the ISO date string to a more readable format
 */
private fun formatDate(isoDate: String): String {
    return try {
        // Simple formatting, just extract date part (ex: 2025-12-21T21:22:27Z)
        isoDate.substringBefore("T")
    } catch (e: Exception) {
        isoDate
    }
}

// ==================== PREVIEWS ====================

private val previewArticle = Article(
    id = "1",
    title = "Knicks-Spurs: 4 takeaways as New York captures Emirates NBA Cup - NBA",
    description = "New York's depth delivers big, Karl-Anthony Towns returns for the moment and the Knicks hope to flip the Cup script.",
    imageUrl = null,
    url = "https://example.com/article",
    publishedAt = "2025-12-19T10:00:00Z",
    sourceName = "Heat.com"
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewsDetailContentPreview() {
    InstantNewsTheme {
        NewsDetailContent(
            article = previewArticle,
            onOpenInBrowser = {}
        )
    }
}
