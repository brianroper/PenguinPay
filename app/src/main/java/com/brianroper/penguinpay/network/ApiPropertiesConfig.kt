package com.brianroper.penguinpay.network

import android.content.Context
import com.brianroper.penguinpay.R
import java.util.*

class ApiPropertiesConfig(private val context: Context, private val properties: Properties) {
    init {
        loadProperties()
    }

    private fun loadProperties() {
        properties.load(context.resources.openRawResource(R.raw.api))
    }

    val apiProperties = {
        properties
    }

    val baseUrl = {
        properties.getProperty("base_url")
    }

    val key = {
        properties.getProperty("key")
    }
}