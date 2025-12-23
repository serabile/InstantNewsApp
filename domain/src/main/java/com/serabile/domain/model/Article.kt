package com.serabile.domain.model

data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val url: String,
    val publishedAt: String,
    val sourceName: String,
)
