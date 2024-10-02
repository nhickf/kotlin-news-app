package com.grpcx.androidtask.presentation.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.grpcx.androidtask.domain.model.Article
import com.grpcx.androidtask.domain.usecase.UseCase
import com.grpcx.androidtask.presentation.base.BaseViewModel
import com.grpcx.androidtask.util.RepositoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: UseCase) :
    BaseViewModel<MainUiState, MainUiEvent, MainUiEffect>(
        initialState = MainUiState(),
        reducer = MainReducer()
    ) {

    val mainState = state
        .onStart {
            retrieveCacheArticles()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MainUiState()
        )

    val mainUiEffect = effect
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
        )


    suspend fun retrieveLatestNews() {
        sendEvent(MainUiEvent.UpdateOnLoading(true))
        retrieveLatestArticles()
    }

    private suspend fun retrieveLatestArticles() {
        when (val articles = useCase.latestArticles.invoke()) {
            is RepositoryResponse.Error -> sendEvent(
                MainUiEvent.ShowError(articles.message ?: "Something went wrong")
            )

            is RepositoryResponse.Exception -> sendEvent(
                MainUiEvent.ShowError(
                    articles.e.message ?: "Something went wrong"
                )
            )

            is RepositoryResponse.Success -> sendEvent(
                MainUiEvent.UpdateOnArticles(articles.data)
            )
        }
    }

    private suspend fun retrieveCacheArticles() {
        when (val cache = useCase.cacheArticles.invoke()) {
            is RepositoryResponse.Error -> sendEvent(
                MainUiEvent.ShowError(
                    cache.message ?: "Something went wrong"
                )
            )

            is RepositoryResponse.Exception -> sendEvent(
                MainUiEvent.ShowError(
                    cache.e.message ?: "Something went wrong"
                )
            )

            is RepositoryResponse.Success -> sendEvent(
                MainUiEvent.UpdateOnArticles(
                    cache.data
                )
            )
        }
    }


}