package com.serabile.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.serabile.designsystem.theme.InstantNewsTheme
import coil.request.ImageRequest

/**
 * Image component with loading and error states
 */
@Composable
fun NewsImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            ImagePlaceholder(
                modifier = Modifier.fillMaxSize(),
                isLoading = true
            )
        },
        error = {
            ImagePlaceholder(
                modifier = Modifier.fillMaxSize(),
                isLoading = false
            )
        }
    )
}

/**
 * Placeholder for image when loading or error
 */
@Composable
internal fun ImagePlaceholder(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = Icons.Filled.BrokenImage,
                contentDescription = stringResource(com.serabile.designsystem.R.string.error),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun ImagePlaceholderLoadingPreview() {
    InstantNewsTheme {
        ImagePlaceholder(
            modifier = Modifier.size(200.dp),
            isLoading = true
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun ImagePlaceholderErrorPreview() {
    InstantNewsTheme {
        ImagePlaceholder(
            modifier = Modifier.size(200.dp),
            isLoading = false
        )
    }
}
