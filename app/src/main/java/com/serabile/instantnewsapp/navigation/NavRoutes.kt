package com.serabile.instantnewsapp.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation routes in the app
 */
sealed interface NavRoutes {

    @Serializable
    data object TopNews : NavRoutes

    @Serializable
    data class NewsDetail(
        val id: String,
        val title: String,
        val description: String?,
        val imageUrl: String?,
        val url: String,
        val publishedAt: String?,
        val sourceName: String?,
    ) : NavRoutes
}
