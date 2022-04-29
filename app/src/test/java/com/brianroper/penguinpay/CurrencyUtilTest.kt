package com.brianroper.penguinpay

import com.brianroper.penguinpay.utils.CurrencyUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyUtilTest {

    @Test
    fun `insert binary value and get correct value in usd`() {
        val bin = 1000101001
        val usdValue = CurrencyUtil.convertBinaryToUsd(bin.toString())
        assertThat(usdValue).isEqualTo(553)
    }

    @Test
    fun `insert binary value and get value in usd, expect error`() {
        val bin = 1000101001
        val usdValue = CurrencyUtil.convertBinaryToUsd(bin.toString())
        assertThat(usdValue).isNotEqualTo(77777)
    }

    @Test
    fun `insert decimal value and get correct binary value`() {
        val value = 5687.00
        val bin = CurrencyUtil.convertToBinary(value)
        assertThat(bin).isEqualTo("1011000110111")
    }

    @Test
    fun `insert decimal value and get binary value, expect error`() {
        val value = 5687.00
        val bin = CurrencyUtil.convertToBinary(value)
        assertThat(bin).isNotEqualTo("101010011")
    }

    @Test
    fun `enter usd and get expected currency value`() {
        val usd = 1000.00
        val rate = CurrencyUtil.getExchangeRate("Kenya")
        val exchanged = CurrencyUtil.calculateExchangeRate(usd, rate)
        assertThat(exchanged).isEqualTo(115853.00)
    }
}