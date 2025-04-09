package com.example.appgorjeta

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appgorjeta.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

// Valor total da conta
    // Número de pessoas
    // Porcentagem da gorjeta
    // 10%, 15% ou 20%
    // Calcular
    // Limpar

    // Recuperar as views do layout
    // ViewBinding

    // Recuperar os radio buttons
    // Calculo de tip
    // Mostrar resultado

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var percentage: Int = 0
        binding.rbOptionOne.setOnCheckedChangeListener { _, ischecked ->
            println("Roque1  Option one: $ischecked")
            if (ischecked) {
                percentage = 10
            }
        }
        binding.rbOptionOne.setOnCheckedChangeListener { _, ischecked ->
            println("Roque1  Option two: $ischecked")
            if (ischecked) {
                percentage = 15
            }
        }
        binding.rbOptionOne.setOnCheckedChangeListener { _, ischecked ->
            println("Roque1  Option three: $ischecked")
            if (ischecked) {
                percentage = 20
            }
        }
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.num_people,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerNumberOfPeople.adapter = adapter
var numOfPeopleSelected = 0
        binding.spinnerNumberOfPeople.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id:Long
                ) {
                    numOfPeopleSelected = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.btnDone.setOnClickListener {
            val totalTabletemp = binding.tieTotal.text

            if (totalTabletemp?.isEmpty() == true
            ) {
                Snackbar
                    .make(binding.tieTotal, "Preencha todos os campos", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                val totalTable: Float = totalTabletemp.toString().toFloat()
                val nPeople: Int = numOfPeopleSelected

                val totalTemp = totalTable / nPeople
                val tips = totalTemp * percentage / 100
                val totalWithTips = totalTemp + tips

                val intent = Intent(this, SummaryActivity::class.java)
                intent.apply {
                    putExtra("totalTable", totalTable)
                    putExtra("numPeople", numOfPeopleSelected)
                    putExtra("percentage", percentage)
                    putExtra("totalAmount", totalWithTips)
                }
                startActivity(intent)



            }

            binding.btnClean.setOnClickListener {

                binding.tieTotal.setText("")
                binding.rbOptionThree.isChecked = false
                binding.rbOptionTwo.isChecked = false
                binding.rbOptionOne.isChecked = false


            }
        }

    }
}