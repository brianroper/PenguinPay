package com.brianroper.penguinpay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brianroper.penguinpay.repositories.ApiNetworkContract
import com.brianroper.penguinpay.utils.CurrencyUtil
import com.brianroper.penguinpay.network.LatestResponse
import com.brianroper.penguinpay.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiNetworkContract) : ViewModel() {
    private val _rates = MutableLiveData<Resource<LatestResponse>>()
    val rates : LiveData<Resource<LatestResponse>>
        get() = _rates

    private val _total = MutableLiveData<String>()
    val total: LiveData<String>
        get() = _total

    fun updateTotal(binString: String, country: String) {
        viewModelScope.launch {
            val usdValue = CurrencyUtil.convertBinaryToUsd(binString) //9 from 1001
            val currencyCode = CurrencyUtil.getCurrencyCode(country)
            val exchangeRate = CurrencyUtil.getExchangeRate(currencyCode)
            if (usdValue != null && exchangeRate != null) {
                val converted = CurrencyUtil.calculateExchangeRate(usdValue, exchangeRate!!)
                _total.postValue(converted.toString())
            }
        }
    }

    fun send(binString: String, country: String) {
        //send to recipient
    }

    fun getLatestRates() {
        _rates.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.getLatestRates()
            _rates.value = response
        }
    }

    fun updateRates(rates: LatestResponse.Rates?) {
        CurrencyUtil.rates = rates
    }
}