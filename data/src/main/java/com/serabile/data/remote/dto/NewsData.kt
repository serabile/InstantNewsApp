package com.serabile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceData(
    @SerialName("id")
    val id: String?,
    @SerialName("name")
    val name: String,
)

@Serializable
data class ArticleData(
    @SerialName("source")
    val source: SourceData,
    @SerialName("author")
    val author: String?,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String?,
    @SerialName("url")
    val url: String,
    @SerialName("urlToImage")
    val urlToImage: String?,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("content")
    val content: String?,
)

@Serializable
data class NewsResponseData(
    @SerialName("status")
    val status: String,
    @SerialName("totalResults")
    val totalResults: Int,
    @SerialName("articles")
    val articles: List<ArticleData>,
)
