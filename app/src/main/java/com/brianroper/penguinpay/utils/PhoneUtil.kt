package com.brianroper.penguinpay.utils

object PhoneUtil {
    fun getPhonePattern(countryCode: String): String {
        return when(countryCode) {
            "Kenya" -> {
                "+254 (###) ###-####"
            }
            "Nigeria" -> {
                "+234 ###-####"
            }
            "Tanzania" -> {
                "+255 (###) ###-####"
            }
            else -> {
                "+256 ###-####"
            }
        }
    }
}