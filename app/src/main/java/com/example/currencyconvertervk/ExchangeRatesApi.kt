package com.example.currencyconvertervk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ExchangeRatesAPI {
    private const val BASE_URL = "https://api.exchangeratesapi.io/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ExchangeRatesService::class.java)

    fun getExchangeRates(baseCurrency: String): Call<ExchangeRatesResponse> {
        return service.getExchangeRates(baseCurrency)
    }
}

fun fetchCurrencyExchangeRates(baseCurrency: String, targetCurrency: String) {
    val call = ExchangeRatesAPI.getExchangeRates(baseCurrency)
    call.enqueue(object : Callback<ExchangeRatesResponse> {
        override fun onResponse(call: Call<ExchangeRatesResponse>, response: Response<ExchangeRatesResponse>) {
            if (response.isSuccessful) {
                val exchangeRates = response.body()?.rates
                val rate = exchangeRates?.get(targetCurrency)
                println("Курс из $baseCurrency в $targetCurrency: $rate")
            } else {
                println("Ошибка при получении курса валют: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<ExchangeRatesResponse>, t: Throwable) {
            println("Ошибка при выполнении запроса: ${t.message}")
        }
    })
}
