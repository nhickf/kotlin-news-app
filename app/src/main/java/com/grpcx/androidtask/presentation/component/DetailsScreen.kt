package com.grpcx.androidtask.presentation.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun DetailScreen(
    url: String
) {
    var loadingProgress by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        WebViewLoader(loadingProgress)
        WebView(
            url = url,
            onWebViewLoaded = {
                loadingProgress = it
            }
        )
    }
}

@Composable
private fun WebViewLoader(progress: Int) {
    val isLoaded by remember(progress) { derivedStateOf { progress >= 100 } }
    if (!isLoaded) {
        LinearProgressIndicator(
            progress = { progress.toFloat() / 100 },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebView(
    url: String,
    onWebViewLoaded: (Int) -> Unit = {}
) {

    AndroidView(
        factory = {
            WebView(it).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.settings.javaScriptEnabled = true
                this.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        onWebViewLoaded(newProgress)
                    }
                }
            }
        },
        update = {
            it.loadUrl(url)
        }
    )
}

