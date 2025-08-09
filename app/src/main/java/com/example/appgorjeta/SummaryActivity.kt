package com.example.appgorjeta

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appgorjeta.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados da Intent AQUI, logo no início
        val totalTable = intent.getFloatExtra("totalTable", 0.0f)
        val numPeople = intent.getFloatExtra("numPeople", 0f)
        val percentage = intent.getFloatExtra("percentage", 0f)
        val totalAmount = intent.getFloatExtra("totalAmount", 0.0f)

        binding.tvPercentage.text = percentage.toString() + "%"
        binding.tvTotalAmount.text = totalAmount.toString()
        binding.tvTotalTable.text = totalAmount.toString()
        binding.tvNumPeopleLabel.text = numPeople.toString()


        binding.btnRefresh.setOnClickListener {
            finish()
        }


        println("Roquere1 $totalAmount")

        // Aplicação de padding conforme as insets do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}



