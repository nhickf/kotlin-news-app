package com.grpcx.androidtask.util

import kotlinx.serialization.Serializable


interface BaseResponse<T:Any>

@Serializable
sealed class NetworkResponse<T:Any> : BaseResponse<T> {
    class Error<T:Any>(val code: Int, val message: String?): NetworkResponse<T>()
    class Success<T:Any>(val data: T): NetworkResponse<T>()
    class Exception<T:Any>(val e: Throwable): NetworkResponse<T>()
}

sealed class RepositoryResponse<T:Any> : BaseResponse<T> {
    class Error<T:Any>(val code: Int, val message: String?): RepositoryResponse<T>()
    class Success<T:Any>(val data: T): RepositoryResponse<T>()
    class Exception<T:Any>(val e: Throwable): RepositoryResponse<T>()
}
