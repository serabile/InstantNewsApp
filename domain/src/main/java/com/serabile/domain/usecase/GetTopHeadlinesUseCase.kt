package com.serabile.domain.usecase

import com.serabile.domain.model.Article
import com.serabile.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {
    suspend operator fun invoke(): Result<List<Article>> = newsRepository.getTopHeadlines()
}
