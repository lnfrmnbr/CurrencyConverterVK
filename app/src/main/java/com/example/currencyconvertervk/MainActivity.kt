package com.example.currencyconvertervk

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.currencyconvertervk.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class ExchangeRatesResponse(
    val status: Int,
    val message: String,
    val data: CurrencyRates
)

data class CurrencyRates(
    val USDRUB: String
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
        Log.e("DEBUG","ff")

        convertButton.setOnClickListener{
            val currInputVal = currInput.text.toString()
            val currOutputVal = currOutput.text.toString()
            val currNumInputVal = currNumInput.text.toString().toInt()
            if (currInputVal+currOutputVal in listOfAvailableExchanges){
                getExchangeRates(currInputVal+currOutputVal,currNumInputVal,currNumOutput)
            }
            else{
                getExchangeRates(currOutputVal+currInputVal,currNumInputVal,currNumOutput)/////////
            }
            }

        }

    }
    private fun getExchangeRates(currency: String, amount: Int, textView: TextView){
        val api = Retrofit.Builder()
            .baseUrl("https://currate.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
        Log.e("DEBUG","ygrt0")
        api.getData(currency).enqueue(object : Callback<ExchangeRatesResponse>{
            override fun onResponse(
                call: Call<ExchangeRatesResponse>,
                response: Response<ExchangeRatesResponse>
            ) {
                Log.e("DEBUG","0")
                if(response.isSuccessful){
                    Log.e("DEBUG","1")
                    response.body()?.let{
                        textView.text = (it.data.USDRUB.toDouble()*amount).toString()
                        Log.e("DEBUG","onresp $it")
                    }
            }
        }

            override fun onFailure(call: Call<ExchangeRatesResponse>, t: Throwable) {
                Log.e("DEBUG", "onFammilure: ${t.message}")
            }
        })
}