package com.brianroper.penguinpay.network

import com.brianroper.penguinpay.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class ExchangeRateHttpClient {
    fun getHttpClient(token: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(TimeoutManager.connectionTimeout, TimeUnit.SECONDS)
        builder.writeTimeout(TimeoutManager.writeTimeout, TimeUnit.SECONDS)
        builder.readTimeout(TimeoutManager.readTimeout, TimeUnit.SECONDS)
        builder.addInterceptor(HeaderInterceptor(token))

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    private class HeaderInterceptor(val token: String): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization", "Token $token")
            return chain.proceed(requestBuilder.build())
        }
    }
}