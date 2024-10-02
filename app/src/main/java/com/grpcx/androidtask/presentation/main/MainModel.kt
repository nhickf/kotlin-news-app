package com.grpcx.androidtask.presentation.main

import com.grpcx.androidtask.domain.model.Article
import com.grpcx.androidtask.presentation.base.Reducer
import javax.annotation.concurrent.Immutable

@Immutable
data class MainUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val selectedArticle: Article? = null,
    val error: String? = null
) : Reducer.ViewState

@Immutable
sealed class MainUiEvent : Reducer.ViewEvent {
    data class UpdateOnLoading(val isLoading: Boolean) : MainUiEvent()
    data class UpdateOnArticles(val articles: List<Article>) : MainUiEvent()
    data class UpdateSelectedArticle(val article: Article) : MainUiEvent()
    data class ShowError(val message: String) : MainUiEvent()
}

@Immutable
sealed class MainUiEffect : Reducer.ViewEffect {
    data class NavigateToDetails(val article: Article) : MainUiEffect()
}

class MainReducer : Reducer<MainUiState, MainUiEvent, MainUiEffect> {

    override fun reduce(
        previousState: MainUiState,
        event: MainUiEvent
    ): Pair<MainUiState, MainUiEffect?> {
        return when (event) {
            is MainUiEvent.UpdateOnArticles -> previousState.copy(
                articles = event.articles,
                isLoading = false,
                error = null
            ) to null

            is MainUiEvent.UpdateOnLoading -> previousState.copy(
                isLoading = event.isLoading,
                articles = previousState.articles,
                error = null
            ) to null

            is MainUiEvent.UpdateSelectedArticle -> previousState.copy(
                selectedArticle = event.article
            ) to MainUiEffect.NavigateToDetails(
                event.article
            )

            is MainUiEvent.ShowError -> previousState.copy(
                error = event.message,
                isLoading = false
            ) to null
        }
    }

}