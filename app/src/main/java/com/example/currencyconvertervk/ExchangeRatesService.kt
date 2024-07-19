package com.example.currencyconvertervk
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesService {
    @GET("latest")
    fun getExchangeRates(@Query("base") baseCurrency: String): Call<ExchangeRatesResponse>
}
