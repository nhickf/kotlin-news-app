package com.grpcx.androidtask.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.grpcx.androidtask.R
import com.grpcx.androidtask.domain.model.Article
import com.grpcx.androidtask.domain.model.Source
import com.grpcx.androidtask.presentation.main.MainUiEffect
import com.grpcx.androidtask.presentation.main.MainViewModel
import com.grpcx.androidtask.util.items
import com.grpcx.androidtask.util.timeAgo
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetails: (Article) -> Unit,
) {

    val compositionAwareScope = rememberCoroutineScope()
    val uiState by viewModel.mainState.collectAsStateWithLifecycle()
    val uiEffect by viewModel.mainUiEffect.collectAsStateWithLifecycle(initialValue = null)
    val rememberPullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(uiEffect) {
        when (val effect = uiEffect) {
            is MainUiEffect.NavigateToDetails -> {
                onNavigateToDetails(effect.article)
            }

            else -> {
                //do nothing
            }
        }
    }

    PullToRefreshBox(
        state = rememberPullToRefreshState,
        isRefreshing = uiState.isLoading,
        onRefresh = {
            compositionAwareScope.launch {
                viewModel.retrieveLatestNews()
            }
        }
    ) {
        ArticleList(
            error = uiState.error,
            articles = uiState.articles,
            onNavigateToDetails = {
                viewModel.sendEffect(
                    MainUiEffect.NavigateToDetails(it)
                )
            }
        )
    }
}


@Composable
private fun ArticleList(
    error: String?,
    articles: List<Article>,
    onNavigateToDetails: (Article) -> Unit = {},
) {

    val sortedArticles = articles.sortedByDescending { it.publishedAt }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {

        error?.let {
            item(
                key = "error",
                contentType = "error"
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(),
                    text = it,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(
            items = sortedArticles,
            itemContent = {
                ArticleCard(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 100.dp)
                        .fillMaxWidth(),
                    article = it
                ) {
                    onNavigateToDetails(it)
                }
            },
            emptyContent = {
                EmptyArticle()
            }
        )
    }
}

@Composable
private fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.clickable {
            onClick()
        }
    ) {

        ListItem(
            headlineContent = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(),
                    maxLines = 1,
                    text = article.title,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            },
            supportingContent = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(),
                    overflow = TextOverflow.Ellipsis,
                    text = article.description.orEmpty()
                )

            },
            overlineContent = {
                timeAgo(article.publishedAt)?.let { Text(it) }
            },
            trailingContent = {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(100.dp),
                    model = article.urlToImage,
                    error = {
                        Image(
                            painter = painterResource(R.drawable.baseline_image_24),
                            contentDescription = null
                        )
                    },
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                )
            }
        )
    }
}

@Composable
private fun EmptyArticle() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(R.drawable.baseline_newspaper_24),
            contentDescription = "Articles"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            text = "No articles found",
        )
    }
}

@Preview
@Composable
private fun MainPreview() {
    ArticleList(
        error = null,
        articles = listOf(
            Article(
                author = "Samantha Kelly",
                content = "It's about to get easier for US merchants to use cryptocurrency for payments. PayPal is launching a new service to allow the businesses that use its platform to buy, hold and sell crypto. \r\nAlthough … [+1029 chars]",
                description = "The change will bring bitcoin and other virtual coins to \"millions\" of merchants across the US.",
                publishedAt = "2024-09-26T15:12:00Z",
                source = Source(
                    id = "null",
                    name = "CNET"
                ),
                title = "PayPal to Allow Businesses Buy and Sell Crypto",
                urlToImage = "https://www.cnet.com/a/img/resize/2b2ba45973ffae7baacd4449b399bd35435e4fa0/hub/2022/04/22/d7acb748-e7bd-4553-ae44-2f893ccb87ba/paypal-logo-2022-659.jpg?auto=webp&fit=crop&height=675&width=1200",
                url = "https://www.cnet.com/personal-finance/crypto/paypal-to-allow-businesses-buy-and-sell-crypto/"
            )
        )
    )
}