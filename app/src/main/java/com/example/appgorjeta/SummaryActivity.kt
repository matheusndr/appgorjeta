package com.example.appgorjeta

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary)

        // Recupera os dados da Intent AQUI, logo no início
        val totalTable = intent.getFloatExtra("totalTable", 0.0f)
        val numPeople = intent.getFloatExtra("numPeople", 0f)
        val percentage = intent.getFloatExtra("percentage", 0f)
        val totalAmount = intent.getFloatExtra("totalAmount", 0.0f)

        println("Roquere1 $totalAmount")

        // Aplicação de padding conforme as insets do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}



