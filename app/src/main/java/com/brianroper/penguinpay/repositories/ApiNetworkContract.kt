package com.brianroper.penguinpay.repositories

import com.brianroper.penguinpay.network.LatestResponse
import com.brianroper.penguinpay.Resource

interface ApiNetworkContract {
    suspend fun getLatestRates() : Resource<LatestResponse>
}