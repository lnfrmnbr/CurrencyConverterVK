package com.example.currencyconvertervk

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.currencyconvertervk.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText

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


        convertButton.setOnClickListener{
            val currInputVal = currInput.text
            val currOutputVal = currOutput.text
            val currNumInputVal = currNumInput.text
        }

    }
}