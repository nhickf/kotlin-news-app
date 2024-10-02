package com.grpcx.androidtask.data.source.network

import com.grpcx.androidtask.util.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResponse<Type>>> {
    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResponse<Type>> {
        return NetworkResponseCall(call)
    }
}

class NetworkResponseCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        resultType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (getRawType(resultType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, resultType as ParameterizedType)
        if (getRawType(callType) != NetworkResponse::class.java) {
            return null
        }

        val result = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResponseCallAdapter(result)

    }

    companion object {
        @JvmStatic
        fun create(): NetworkResponseCallAdapterFactory  = NetworkResponseCallAdapterFactory()
    }

}