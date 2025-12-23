package com.serabile.data.mapper

import com.serabile.data.remote.dto.ArticleData
import com.serabile.domain.model.Article

fun ArticleData.toDomain(): Article = Article(
    id = url.hashCode().toString(),
    title = title,
    description = description,
    imageUrl = urlToImage,
    url = url,
    publishedAt = publishedAt,
    sourceName = source.name,
)

fun List<ArticleData>.toDomain(): List<Article> = this.map { it.toDomain() }
