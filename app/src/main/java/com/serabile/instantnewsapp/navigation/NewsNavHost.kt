package com.serabile.instantnewsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.serabile.domain.model.Article
import com.serabile.topnews.screen.NewsDetailScreen
import com.serabile.topnews.screen.TopNewsScreen

/**
 * Main navigation graph
 */
@Composable
fun NewsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.TopNews,
        modifier = modifier
    ) {
        composable<NavRoutes.TopNews> {
            TopNewsScreen(
                onArticleClick = { article ->
                    navController.navigate(
                        NavRoutes.NewsDetail(
                            id = article.id,
                            title = article.title,
                            description = article.description,
                            imageUrl = article.imageUrl,
                            url = article.url,
                            publishedAt = article.publishedAt,
                            sourceName = article.sourceName
                        )
                    )
                }
            )
        }
        
        composable<NavRoutes.NewsDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoutes.NewsDetail>()
            val article = Article(
                id = route.id,
                title = route.title,
                description = route.description,
                imageUrl = route.imageUrl,
                url = route.url,
                publishedAt = route.publishedAt.orEmpty(),
                sourceName = route.sourceName.orEmpty()
            )
            NewsDetailScreen(
                article = article,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
