package com.brianroper.penguinpay.network

import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("latest.json")
    suspend fun getLatestRates(): Response<LatestResponse>
}