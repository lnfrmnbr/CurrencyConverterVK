package com.example.currencyconvertervk

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
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
import retrofit2.create
import retrofit2.http.GET


data class ExchangeRatesResponse(
    val rates: Map<String, Double>
)

class CurrencyExchange(
    val baseCurrency: String,
    val targetCurrency: String
)

interface CurrencyApi{

    @GET("?get=rates&pairs=USDRUB&key=170b2bf0625601c09c371bae2a79b8ed")
    fun getData(): Call<List<CurrencyExchange>>
}

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
        val currency = resources.getStringArray(R.array.currency)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, currency)
        binding.currInput.setAdapter(arrayAdapter)
        binding.currOutput.setAdapter(arrayAdapter)
        Log.e("DEBUG","ff")
        getExchangeRates()


        convertButton.setOnClickListener{
            val currInputVal = currInput.text.toString()
            val currOutputVal = currOutput.text.toString()
            val currNumInputVal = currNumInput.text

            }

        }

    }
    private fun getExchangeRates(){
        val api = Retrofit.Builder()
            .baseUrl("https://currate.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
        Log.e("DEBUG","ygrt0")
        api.getData().enqueue(object : Callback<List<CurrencyExchange>>{
            override fun onResponse(
                call: Call<List<CurrencyExchange>>,
                response: Response<List<CurrencyExchange>>
            ) {
                Log.e("DEBUG","0")
                if(response.isSuccessful){
                    Log.e("DEBUG","1")
                    response.body()?.let{
                        for (i in it){
                            Log.e("DEBUG","onresp $i")
                        }
                    }
            }
        }

            override fun onFailure(call: Call<List<CurrencyExchange>>, t: Throwable) {
                Log.e(",", "onFammilure: ${t.message}")
            }
        })
}