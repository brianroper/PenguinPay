package com.brianroper.penguinpay.repositories

import com.brianroper.penguinpay.network.ExchangeRateApi
import com.brianroper.penguinpay.network.LatestResponse
import com.brianroper.penguinpay.Resource
import java.lang.Exception
import javax.inject.Inject

class ApiNetworkRepository @Inject constructor(private val api: ExchangeRateApi):
    ApiNetworkContract {
    override suspend fun getLatestRates(): Resource<LatestResponse> {
        return try {
            val response = api.getLatestRates()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.error("No data due to exception, ${e.message}", null)
        }
    }
}