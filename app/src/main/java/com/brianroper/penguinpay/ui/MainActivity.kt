package com.brianroper.penguinpay.ui

import android.R.layout
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brianroper.penguinpay.utils.CurrencyUtil
import com.brianroper.penguinpay.utils.PhoneTextFormatter
import com.brianroper.penguinpay.utils.PhoneUtil
import com.brianroper.penguinpay.Status.*
import com.brianroper.penguinpay.databinding.ActivityMainBinding
import com.brianroper.penguinpay.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val countries = arrayOf("Kenya", "Nigeria", "Tanzania", "Uganda")

    private var phoneTextWatcher: PhoneTextFormatter? = null
    private var totalTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, layout.simple_spinner_item, countries)
        binding.countrySpinner.adapter = adapter
        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updatePhoneInputFormatter()
                resetTotal()
            }
        }

        updatePhoneInputFormatter()

        if (totalTextWatcher == null) {
            totalTextWatcher = object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(editable: Editable?) {
                    if (editable != null && editable.isNotEmpty()) {
                        viewModel.updateTotal(editable.toString(), binding.countrySpinner.selectedItem.toString())
                    } else {
                        binding.currencyConversionTotalTextView.text =
                            "0 ${CurrencyUtil.getCurrencyCode(binding.countrySpinner.selectedItem.toString())}"
                    }
                }
            }
        }

        binding.currencyInputEditText.addTextChangedListener(totalTextWatcher)

        binding.submitButton.setOnClickListener {
            validateAndSend()
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.total.observe(this) {
            binding.currencyConversionTotalTextView.text =
                "$it ${CurrencyUtil.getCurrencyCode(binding.countrySpinner.selectedItem.toString())}"
        }

        viewModel.rates.observe(this, Observer {
            when (it.status) {
                SUCCESS -> viewModel.updateRates(it.data?.rates)
                ERROR -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                LOADING -> Log.d("ExchangeRateApi:: ", "Loading")
            }
        })

        if (CurrencyUtil.rates == null) {
            viewModel.getLatestRates()
        }
    }

    private fun updatePhoneInputFormatter() {
        if (binding.phoneEditText.text.isNotEmpty()) {
            binding.phoneEditText.setText("")
        }

        if (phoneTextWatcher == null) {
            phoneTextWatcher =
                PhoneTextFormatter(binding.phoneEditText)
            binding.phoneEditText.addTextChangedListener(phoneTextWatcher)
        }

        val phonePattern = PhoneUtil.getPhonePattern(binding.countrySpinner.selectedItem.toString())
        phoneTextWatcher?.setPattern(phonePattern)
    }

    private fun resetTotal() {
        val country = binding.countrySpinner.selectedItem.toString()
        binding.currencyConversionTotalTextView.text = "0 ${CurrencyUtil.getCurrencyCode(country)}"
        binding.currencyInputEditText.setText("")
    }

    private fun validateAndSend() {
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        val value = binding.currencyInputEditText.text.toString()
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() && value.isNotEmpty()) {
            viewModel.send(binding.currencyInputEditText.text.toString(), binding.countrySpinner.selectedItem.toString())
            Toast.makeText(this, "Transaction sent!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CurrencyUtil.rates = null
        binding.phoneEditText.removeTextChangedListener(phoneTextWatcher)
        binding.currencyInputEditText.removeTextChangedListener(totalTextWatcher)
    }
}