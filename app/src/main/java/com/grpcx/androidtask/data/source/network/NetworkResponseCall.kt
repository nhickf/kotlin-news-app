package com.grpcx.androidtask.data.source.network

import com.grpcx.androidtask.util.NetworkResponse
import com.grpcx.androidtask.util.handleNetworkCall
import okhttp3.Request
import okio.Timeout

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResponseCall<T : Any>(
    private val call: Call<T>
) : Call<NetworkResponse<T>> {

    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
         call.enqueue(object : Callback<T> {
            override fun onResponse(mCall: Call<T>, response: Response<T>) {
                val networkResponse = handleNetworkCall { response }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }

            override fun onFailure(mCall: Call<T>, e: Throwable) {
                val networkResult = NetworkResponse.Exception<T>(e)
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResult))
            }
        })
    }

    override fun clone(): Call<NetworkResponse<T>> {
        return NetworkResponseCall(call.clone())
    }

    override fun execute(): Response<NetworkResponse<T>> {
        return call.execute().let {
            Response.success(handleNetworkCall { it })
        }
    }

    override fun isExecuted(): Boolean {
        return call.isExecuted
    }

    override fun cancel() {
        return call.cancel()
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    override fun request(): Request {
        return call.request()
    }

    override fun timeout(): Timeout {
        return call.timeout()
    }


}