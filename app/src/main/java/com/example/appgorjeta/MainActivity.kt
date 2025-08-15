package com.example.appgorjeta

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.appgorjeta.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences
    private val currency: NumberFormat by lazy { NumberFormat.getCurrencyInstance(Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("tip_prefs", MODE_PRIVATE)

        // Restaura último uso
        restoreInputs()

        // Listeners para atualizar preview ao vivo
        binding.tieTotal.addTextChangedListener(watcher)
        binding.tieNumPeople.addTextChangedListener(watcher)

        binding.sliderPercentage.addOnChangeListener { _: Slider, value: Float, _: Boolean ->
            binding.tvPercentageValue.text = String.format(Locale.getDefault(), "%.0f%%", value)
            updatePreview()
        }

        binding.btnDone.setOnClickListener {
            // fecha teclado
            currentFocus?.let { v ->
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(v.windowToken, 0)
            }

            val total = parseFloat(binding.tieTotal.text)
            val people = parseFloat(binding.tieNumPeople.text)
            val percent = binding.sliderPercentage.value

            if (!validate(total, people)) return@setOnClickListener

            val tip = total!! * (percent / 100f)
            val totalWithTip = total + tip

            // Salva últimos valores
            saveInputs(total, people!!, percent)

            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra("totalTable", total)
                putExtra("numPeople", people)
                putExtra("percentage", percent)
                putExtra("totalAmount", totalWithTip)
            }
            startActivity(intent)
            // animação suave
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.btnClean.setOnClickListener {
            binding.tieTotal.setText("")
            binding.tieNumPeople.setText("")
            binding.sliderPercentage.value = 10f
            clearErrors()
            updatePreview()
        }

        // Primeira atualização
        updatePreview()
    }

    // ---- Preview e validação ----

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { updatePreview() }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun updatePreview() {
        val total = parseFloat(binding.tieTotal.text)
        val people = parseFloat(binding.tieNumPeople.text)
        val percent = binding.sliderPercentage.value

        val valid = validate(total, people, showSnack = false)

        if (valid) {
            val tip = total!! * (percent / 100f)
            val totalWithTip = total + tip
            val perPerson = totalWithTip / people!!

            binding.tvPreviewTip.text = getString(R.string.preview_tip, currency.format(tip))
            binding.tvPreviewTotal.text = getString(R.string.preview_total, currency.format(totalWithTip))
            binding.tvPreviewPerPerson.text = getString(R.string.preview_per_person, currency.format(perPerson))
        } else {
            binding.tvPreviewTip.text = getString(R.string.preview_tip, "--")
            binding.tvPreviewTotal.text = getString(R.string.preview_total, "--")
            binding.tvPreviewPerPerson.text = getString(R.string.preview_per_person, "--")
        }

        binding.btnDone.isEnabled = valid
    }

    private fun validate(total: Float?, people: Float?, showSnack: Boolean = true): Boolean {
        var ok = true

        if (total == null || total <= 0f) {
            binding.tilTotal.error = getString(R.string.error_total_amount)
            if (showSnack) showSnack(getString(R.string.error_total_amount))
            ok = false
        } else {
            binding.tilTotal.error = null
        }

        if (people == null || people < 1f) {
            binding.tilNumPeople.error = getString(R.string.error_num_people)
            if (showSnack) showSnack(getString(R.string.error_num_people))
            ok = false
        } else {
            binding.tilNumPeople.error = null
        }

        return ok
    }

    private fun clearErrors() {
        binding.tilTotal.error = null
        binding.tilNumPeople.error = null
    }

    private fun parseFloat(text: CharSequence?): Float? {
        if (text.isNullOrBlank()) return null
        return text.toString().trim().replace(',', '.').toFloatOrNull()
    }

    private fun showSnack(message: String) {
        Snackbar.make(binding.tieTotal, message, Snackbar.LENGTH_SHORT).show()
    }

    // ---- Persistência ----
    private fun saveInputs(total: Float, people: Float, percent: Float) {
        prefs.edit()
            .putFloat("total", total)
            .putFloat("people", people)
            .putFloat("percent", percent)
            .apply()
    }

    private fun restoreInputs() {
        val total = prefs.getFloat("total", 0f)
        val people = prefs.getFloat("people", 4f)
        val percent = prefs.getFloat("percent", 10f)

        if (total > 0f) binding.tieTotal.setText(total.toString())
        binding.tieNumPeople.setText(people.toInt().toString())
        binding.sliderPercentage.value = percent
        binding.tvPercentageValue.text = String.format(Locale.getDefault(), "%.0f%%", percent)
    }
}
