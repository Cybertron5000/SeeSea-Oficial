package com.example.seesea

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuração dos Cards
        val marinaCard = findViewById<CardView>(R.id.marinaCard)
        val fuelCard = findViewById<CardView>(R.id.fuelCard)
        val navigationCard = findViewById<CardView>(R.id.navigationCard)
        val contactCard = findViewById<CardView>(R.id.contactCard)

        marinaCard.setOnClickListener {
            startActivity(Intent(this, MarinaSpotsActivity::class.java))
        }

        fuelCard.setOnClickListener {
            startActivity(Intent(this, FuelStationsActivity::class.java))
        }

        navigationCard.setOnClickListener {
            startActivity(Intent(this, NauticalMapActivity::class.java))
        }

        contactCard.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }

        // Botões de Autenticação
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
