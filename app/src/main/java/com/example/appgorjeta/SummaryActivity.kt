package com.example.appgorjeta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appgorjeta.databinding.ActivitySummaryBinding
import java.text.NumberFormat
import java.util.Locale

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados
        val totalTable = intent.getFloatExtra("totalTable", 0f)
        val numPeople  = intent.getFloatExtra("numPeople", 0f)
        val percentage = intent.getFloatExtra("percentage", 0f)
        val totalAmount = intent.getFloatExtra("totalAmount", 0f)

        // Cálculos
        val tipAmount = totalTable * (percentage / 100f)
        val amountPerPerson = if (numPeople > 0f) totalAmount / numPeople else 0f

        // Formatação monetária
        val currency = NumberFormat.getCurrencyInstance(Locale.getDefault())

        // Preenche a UI (ids conforme seu XML)
        binding.tvTotalTable.text     = currency.format(totalTable)
        binding.tvNumPeopleValue.text = numPeople.toInt().toString()
        binding.tvPercentage.text     = String.format(Locale.getDefault(), "%.0f%%", percentage)
        binding.tvTipValue.text       = currency.format(tipAmount)
        binding.tvPerPerson.text      = currency.format(amountPerPerson)   // <- corresponde a @+id/tv_per_person
        binding.tvTotalAmount.text    = currency.format(totalAmount)

        binding.btnRefresh.setOnClickListener { finish() }
    }
}
