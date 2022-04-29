package com.brianroper.penguinpay.dependencyinjection

import android.content.Context
import com.brianroper.penguinpay.R
import com.brianroper.penguinpay.network.ApiPropertiesConfig
import com.brianroper.penguinpay.network.ExchangeRateApi
import com.brianroper.penguinpay.network.ExchangeRateHttpClient
import com.brianroper.penguinpay.repositories.ApiNetworkContract
import com.brianroper.penguinpay.repositories.ApiNetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectProperties() = Properties()

    @Singleton
    @Provides
    fun injectApiProperties(@ApplicationContext context: Context, properties: Properties) = ApiPropertiesConfig(context, properties)

    @Singleton
    @Provides
    fun injectRetrofitAPI(@ApplicationContext context: Context) : ExchangeRateApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(context.getString(R.string.base_url))
            .client(ExchangeRateHttpClient().getHttpClient(context.getString(R.string.token)))
            .build()
            .create(ExchangeRateApi::class.java)
    }

    @Singleton
    @Provides
    fun injectApiNetworkRepository(api: ExchangeRateApi) = ApiNetworkRepository(api) as ApiNetworkContract
}