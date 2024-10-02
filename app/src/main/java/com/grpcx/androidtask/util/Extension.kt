package com.grpcx.androidtask.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun <T : Any> handleNetworkCall(
    execute: () -> Response<T>
): NetworkResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResponse.Success(body)
        } else {
            NetworkResponse.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        NetworkResponse.Error(e.code(), e.message())
    } catch (e: Exception) {
        NetworkResponse.Exception(e)
    }
}


suspend fun <T : Any> NetworkResponse<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResponse<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Error<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> NetworkResponse<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Exception<T>) {
        executable(e)
    }
}

inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}

inline fun <T> LazyListScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit,
    crossinline emptyContent: @Composable LazyItemScope.() -> Unit
) = run {
    if (items.isNotEmpty()) {
        items(
            count = items.size,
            key = if (key != null) { index: Int -> key(items[index]) } else null,
            contentType = { index: Int -> contentType(items[index]) }
        ) {
            itemContent(items[it])
        }
    } else {
        item {
            emptyContent()
        }
    }
}