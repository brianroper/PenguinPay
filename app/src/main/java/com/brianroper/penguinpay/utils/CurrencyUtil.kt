package com.brianroper.penguinpay.utils

import com.brianroper.penguinpay.network.LatestResponse
import kotlin.math.round

object CurrencyUtil {
    var rates: LatestResponse.Rates? = null

    /**
     * takes a binary string and returns a double in usd currency
     */
    fun convertBinaryToUsd(bin: String): Double {
        return bin.trim().toInt(2).toDouble()
    }

    /**
     * round to ensure we only receive 2 decimal places
     */
    fun calculateExchangeRate(usdValue: Double, exchangeRate: Double): Double {
        return round(usdValue * exchangeRate * 100) / 100
    }

    fun convertToBinary(value: Double): String {
        return Integer.toBinaryString(value.toInt())
    }

    fun getExchangeRate(code: String): Double? {
        return when (code) {
            "KES" -> {
                rates?.KES
            }
            "NGN" -> {
                rates?.NGN
            }
            "TZS" -> {
                rates?.TZS
            }
            else -> {
                rates?.UGX
            }
        }
    }

    fun getCurrencyCode(country: String): String {
        return when (country) {
            "Kenya" -> {
                "KES"
            }
            "Nigeria" -> {
                "NGN"
            }
            "Tanzania" -> {
                "TZS"
            }
            else -> {
                "UGX"
            }
        }
    }
}