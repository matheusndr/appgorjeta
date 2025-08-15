package com.example.appgorjeta

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appgorjeta.databinding.ActivitySummaryBinding
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recebe dados
        val totalTable = intent.getFloatExtra("totalTable", 0f)
        val numPeople  = intent.getFloatExtra("numPeople", 0f)
        val percentage = intent.getFloatExtra("percentage", 0f)
        val totalAmount = intent.getFloatExtra("totalAmount", 0f)

        // Calcula adicionais
        val tipAmount = totalTable * (percentage / 100f)
        val safePeople = max(1f, numPeople)
        val amountPerPerson = totalAmount / safePeople

        // Formata moeda
        fun formatCurrency(value: Float): String {
            val fmt = NumberFormat.getCurrencyInstance(Locale.getDefault())
            return fmt.format(value)
        }

        // Preenche UI
        binding.tvTotalTable.text = formatCurrency(totalTable)
        binding.tvNumPeopleValue.text = numPeople.toInt().toString()
        binding.tvPercentage.text = String.format(Locale.getDefault(), "%.0f%%", percentage)
        binding.tvTipValue.text = formatCurrency(tipAmount)
        binding.tvTotalAmount.text = formatCurrency(totalAmount)
        binding.tvAmountPerPerson.text = formatCurrency(amountPerPerson)

        binding.btnRefresh.setOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom)
            insets
        }
    }
}
