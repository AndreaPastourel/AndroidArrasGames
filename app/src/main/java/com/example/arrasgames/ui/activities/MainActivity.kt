package com.example.arrasgames.ui.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.arrasgames.R
import com.example.arrasgames.ui.fragement.ForfaitsFragment
import com.example.arrasgames.ui.fragments.HistoriqueFragment
import com.google.android.material.bottomnavigation.BottomNavigationView




class MainActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Charger le fragement "Forfait" par defaut
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ForfaitsFragment())
            .commit()

        // Gestion du BottomNavigationView
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_forfaits -> ForfaitsFragment()
                R.id.nav_historique -> HistoriqueFragment()

                else -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
                true
            } ?: false
        }
        }
    }
