package com.example.arrasgames.ui.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arrasgames.R
import com.example.arrasgames.model.Reservation


class HistoriqueDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historique_detail)

        // Récupérer l'objet Forfait passé via l'Intent
        val reservation = intent.getParcelableExtra<Reservation>("reservation")
        if (reservation == null) {
            Toast.makeText(this, "Aucune réservation reçue", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Récupérer les vues du layout
        val textViewNom = findViewById<TextView>(R.id.textViewNomDetail)
        val textViewType = findViewById<TextView>(R.id.textViewTypeDetail)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescriptionDetail)
        val textViewPrix = findViewById<TextView>(R.id.textViewPrixDetail)
        val textViewCode = findViewById<TextView>(R.id.textViewCodeDetail)


        // Remplir les détails
        textViewNom.text = "Forfait : ${reservation.package_name}"
        textViewDescription.text = "Status: ${reservation.status}"
        textViewType.text = "Forfait : ${reservation.type_name}"
        textViewPrix.text = "Réservé le : ${reservation.created}"
        textViewCode.text = "Code de réservation : ${reservation.code}"


    }
}