package com.example.appgorjeta

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.appgorjeta.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDone.setOnClickListener {
            // Fecha teclado para melhor UX
            currentFocus?.let { v ->
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(v.windowToken, 0)
            }

            val totalTable = parseFloat(binding.tieTotal.text)     // Float?
            val numPeople  = parseFloat(binding.tieNumPeople.text) // Float?
            val percentage = parseFloat(binding.tiePercentage.text) // Float?

            // Validações
            when {
                totalTable == null || totalTable <= 0f -> {
                    showSnack(getString(R.string.error_total_amount))
                    return@setOnClickListener
                }
                numPeople == null || numPeople < 1f -> {
                    showSnack(getString(R.string.error_num_people))
                    return@setOnClickListener
                }
                percentage == null || percentage < 0f -> {
                    showSnack(getString(R.string.error_percentage))
                    return@setOnClickListener
                }
            }

            // A partir daqui, garantimos que NÃO são nulos
            val totalTableValue = totalTable!!
            val numPeopleValue = numPeople!!
            val percentageValue = percentage!!

            // Cálculos
            val tipAmount = totalTableValue * (percentageValue / 100f)
            val totalWithTips = totalTableValue + tipAmount

            // Envia dados para a próxima tela
            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra("totalTable", totalTableValue)
                putExtra("numPeople", numPeopleValue)
                putExtra("percentage", percentageValue)
                putExtra("totalAmount", totalWithTips)
            }

            clean()
            startActivity(intent)
        }

        binding.btnClean.setOnClickListener { clean() }
    }

    private fun clean() {
        binding.tieTotal.setText("")
        binding.tiePercentage.setText("")
        binding.tieNumPeople.setText("")
    }

    /**
     * Parsing tolerante: aceita "123,45" ou "123.45".
     */
    private fun parseFloat(text: CharSequence?): Float? {
        if (text.isNullOrBlank()) return null
        val normalized = text.toString().trim().replace(',', '.')
        return normalized.toFloatOrNull()
    }

    private fun showSnack(message: String) {
        Snackbar.make(binding.tieTotal, message, Snackbar.LENGTH_LONG).show()
    }
}
