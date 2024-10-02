package com.grpcx.androidtask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.grpcx.androidtask.presentation.component.DetailScreen
import com.grpcx.androidtask.presentation.component.MainScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class ArticleDetails(val url: String)

@Composable
fun NavigationRoute(
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home
    ) {

        composable<Home> {
            MainScreen {
                navController.navigate(ArticleDetails(it.url))
            }
        }

        composable<ArticleDetails> {
            DetailScreen(it.toRoute<ArticleDetails>().url)
        }
    }
}

