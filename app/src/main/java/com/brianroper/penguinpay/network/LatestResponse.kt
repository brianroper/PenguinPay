package com.brianroper.penguinpay.network

class LatestResponse {
    var disclaimer: String? = null
    var license: String? = null
    var timestamp: Int? = null
    var base: String? = null
    var rates: Rates? = null

    class Rates {
        var KES: Double? = null
        var NGN: Double? = null
        var TZS: Double? = null
        var UGX: Double? = null
    }
}