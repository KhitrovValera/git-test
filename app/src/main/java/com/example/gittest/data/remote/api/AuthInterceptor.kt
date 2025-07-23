package com.example.gittest.data.remote.api

import com.example.gittest.data.local.KeyValueStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor  @Inject constructor(
    private val storage: KeyValueStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = storage.authToken

        val request = chain.request()

        return if (token.isNullOrBlank()) {
            chain.proceed(request)
        } else {
            val newRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            chain.proceed(newRequest)
        }
    }
}