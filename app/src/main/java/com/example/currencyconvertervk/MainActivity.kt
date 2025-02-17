package com.example.currencyconvertervk

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.currencyconvertervk.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import retrofit2.http.Query


data class ExchangeRatesResponse(
    val status: Int,
    val message: String,
    val data: CurrencyRates
)

data class CurrencyRates(
    val USDRUB: String,
    val BYNRUB: String,
    val USDBYN: String,
    val EURRUB: String,
    val EURBYN : String,
    val EURUSD : String
)

interface CurrencyApi {
    @GET("?get=rates&key=170b2bf0625601c09c371bae2a79b8ed")
    fun getData(@Query("pairs") curr: String): Call<ExchangeRatesResponse>
}

val listOfAvailableExchanges = listOf("BCHEUR","BCHGBP","BCHJPY",
    "BCHRUB","BCHUSD","BCHXRP","BTCBCH","BTCEUR","BTCGBP","BTCJPY",
    "BTCRUB","BTCUSD","BTCXRP","BTGUSD","BYNRUB","CADRUB","CHFRUB",
    "CNYEUR","CNYRUB","CNYUSD","ETHEUR","ETHGBP","ETHJPY","ETHRUB",
    "ETHUSD","EURAED","EURAMD","EURBGN","EURBYN","EURGBP","EURJPY",
    "EURKZT","EURRUB","EURTRY","EURUSD","GBPAUD","GBPBYN","GBPJPY",
    "GBPRUB","GELRUB","GELUSD","IDRUSD","JPYAMD","JPYAZN","JPYRUB",
    "LKREUR","LKRRUB","LKRUSD","MDLEUR","MDLRUB","MDLUSD","MMKEUR",
    "MMKRUB","MMKUSD","RSDEUR","RSDRUB","RSDUSD","RUBAED","RUBAMD",
    "RUBAUD","RUBBGN","RUBKZT","RUBMYR","RUBNZD","RUBSGD","RUBTRY",
    "RUBUAH","THBCNY","THBEUR","THBRUB","USDAED","USDAMD","USDAUD",
    "USDBGN","USDBYN","USDCAD","USDGBP","USDILS","USDJPY","USDKGS",
    "USDKZT","USDMYR","USDRUB","USDTHB","USDUAH","USDVND","XRPEUR",
    "XRPGBP","XRPJPY","XRPRUB","XRPUSD","ZECUSD")


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val convertButton = findViewById<Button>(R.id.convert_butt)
        val currNumInput = findViewById<TextInputEditText>(R.id.curr_num_input)

        val currInput = findViewById<AutoCompleteTextView>(R.id.curr_input)
        val currOutput = findViewById<AutoCompleteTextView>(R.id.curr_output)
        val currNumOutput = findViewById<TextView>(R.id.curr_num_output)
        val currency = resources.getStringArray(R.array.currency)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, currency)
        binding.currInput.setAdapter(arrayAdapter)
        binding.currOutput.setAdapter(arrayAdapter)

        convertButton.setOnClickListener{
            val currInputVal = currInput.text.toString()
            val currOutputVal = currOutput.text.toString()
            val currNumInputVal = currNumInput.text.toString().trim()
            if(currInputVal==""||currOutputVal==""||currNumInputVal==""){
                Toast.makeText(this, "Необходимо заполнить все поля", Toast.LENGTH_LONG).show()
            }
            if (currInputVal == currOutputVal){
                Toast.makeText(this, "Валюты должны быть различные", Toast.LENGTH_LONG).show()
            }
            else{
                val currNumInputVal = currNumInputVal.toInt()
                if (currInputVal+currOutputVal in listOfAvailableExchanges){
                    GlobalScope.launch{
                        getExchangeRates(currInputVal+currOutputVal,currNumInputVal,currNumOutput, 1)
                    }
                }
                else{
                    GlobalScope.launch{
                        getExchangeRates(currOutputVal+currInputVal,currNumInputVal,currNumOutput, -1)

                    }
                }
            }
        }

        }

    }
    suspend private fun getExchangeRates(currency: String, amount: Int, textView: TextView, direction:Int){
        val api = Retrofit.Builder()
            .baseUrl("https://currate.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
        api.getData(currency).enqueue(object : Callback<ExchangeRatesResponse>{
            override fun onResponse(
                call: Call<ExchangeRatesResponse>,
                response: Response<ExchangeRatesResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let{
                        if (direction == 1){
                            if(currency == "USDRUB"){
                                textView.text = String.format("%.2f", (it.data.USDRUB.toDouble()*amount))
                            }
                            if(currency == "USDBYN"){
                                textView.text = String.format("%.2f", (it.data.USDBYN.toDouble()*amount))
                            }
                            if(currency == "BYNRUB"){
                                textView.text = String.format("%.2f", (it.data.BYNRUB.toDouble()*amount))
                            }
                            if(currency == "EURRUB"){
                                textView.text = String.format("%.2f", (it.data.EURRUB.toDouble()*amount))
                            }
                            if(currency == "EURBYN"){
                                textView.text = String.format("%.2f", (it.data.EURBYN.toDouble()*amount))
                            }
                            if(currency == "EURUSD"){
                                textView.text = String.format("%.2f", (it.data.EURUSD.toDouble()*amount))
                            }

                        }
                        else{
                            if(currency == "USDRUB"){
                                textView.text = String.format("%.2f", (1/it.data.USDRUB.toDouble()*amount))
                            }
                            if(currency == "USDBYN"){
                                textView.text = String.format("%.2f", (1/it.data.USDBYN.toDouble()*amount))
                            }
                            if(currency == "BYNRUB"){
                                textView.text = String.format("%.2f", (1/it.data.BYNRUB.toDouble()*amount))
                            }
                            if(currency == "EURRUB"){
                                textView.text = String.format("%.2f", (1/it.data.EURRUB.toDouble()*amount))
                            }
                            if(currency == "EURBYN"){
                                textView.text = String.format("%.2f", (1/it.data.EURBYN.toDouble()*amount))
                            }
                            if(currency == "EURUSD"){
                                textView.text = String.format("%.2f", (1/it.data.EURUSD.toDouble()*amount))
                            }
                        }
                    }
            }
        }

            override fun onFailure(call: Call<ExchangeRatesResponse>, t: Throwable) {
                Log.e("DEBUG", "onFailure: ${t.message}")
            }
        })
}