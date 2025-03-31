package com.example.arrasgames.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.arrasgames.R
import com.example.arrasgames.model.Forfait
import com.example.arrasgames.model.ReservationRequest
import com.example.arrasgames.model.ReservationResponse
import com.example.arrasgames.model.TypeForfait
import com.example.arrasgames.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForfaitDetailActivity : AppCompatActivity() {

    private lateinit var spinnerTypes: Spinner
    private var typesList = listOf<TypeForfait>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forfait_detail)

        val forfait = intent.getParcelableExtra<Forfait>("forfait")
        if (forfait == null) {
            Toast.makeText(this, "Aucun forfait reçu", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Récupérer les vues du layout
        val textViewNom = findViewById<TextView>(R.id.textViewNomDetail)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescriptionDetail)
        val textViewPrix = findViewById<TextView>(R.id.textViewPrixDetail)
        val buttonReserver = findViewById<Button>(R.id.buttonReserver)
        spinnerTypes = findViewById(R.id.spinnerTypes)

        // Remplir les détails du forfait
        textViewNom.text = forfait.name
        textViewDescription.text = forfait.description
        textViewPrix.text = "${forfait.price} €"

        // Charger la liste des types de forfait
        loadTypes()

        // Lors du clic sur le bouton "Réserver"
        buttonReserver.setOnClickListener {
            val apiToken = "Bearer " + getSharedPreferences("user_prefs", 0).getString("api_token", "")

            // Vérifier si le token est vide
            if (apiToken.isBlank() || apiToken == "Bearer ") {
                Toast.makeText(this, "Token manquant", Toast.LENGTH_SHORT).show()
                Log.e("ForfaitDetailDebug", "Token API manquant")
                return@setOnClickListener
            }

            Log.d("ForfaitDetailDebug", "Token utilisé : $apiToken")

            val selectedTypeIndex = spinnerTypes.selectedItemPosition
            if (selectedTypeIndex < 0 || typesList.isEmpty()) {
                Toast.makeText(this, "Aucun type sélectionné", Toast.LENGTH_SHORT).show()
                Log.e("ForfaitDetailDebug", "Erreur : Aucun type sélectionné, index=$selectedTypeIndex")
                return@setOnClickListener
            }

            val selectedTypeId = typesList[selectedTypeIndex].type_id
            Log.d("ForfaitDetailDebug", "Type ID sélectionné : $selectedTypeId")

            val reservationRequest = ReservationRequest(forfait.id, selectedTypeId)

            Log.d("ForfaitDetailDebug", "Envoi de la réservation: package_id=${forfait.id}, type_id=$selectedTypeId")

            ApiClient.instance.reserverForfait(apiToken, reservationRequest).enqueue(object : Callback<ReservationResponse> {
                override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                    Log.d("ForfaitDetailDebug", "Réponse brute : ${response.body()}")

                    if (response.isSuccessful) {
                        val code = response.body()?.code ?: "Erreur"
                        Toast.makeText(this@ForfaitDetailActivity, "Réservé avec le code : $code", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ForfaitDetailDebug", "Erreur API : $errorBody")

                        if (errorBody != null && errorBody.contains("<html")) {
                            Log.e("ForfaitDetailDebug", "L'API renvoie du HTML au lieu du JSON")
                        } else {
                            Log.e("ForfaitDetailDebug", "Réponse API inattendue : $errorBody")
                        }
                        Toast.makeText(this@ForfaitDetailActivity, "Erreur de réservation", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                    Toast.makeText(this@ForfaitDetailActivity, "Erreur réseau : ${t.message}", Toast.LENGTH_LONG).show()
                    Log.e("ForfaitDetailDebug", "Erreur de réservation : ${t.message}")
                }
            })
        }
    }

    private fun loadTypes() {
        ApiClient.instance.getTypes().enqueue(object : Callback<List<TypeForfait>> {
            override fun onResponse(call: Call<List<TypeForfait>>, response: Response<List<TypeForfait>>) {
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        typesList = body.filter { true } // 🔥 Filtre les types invalides
                        Log.d("ForfaitDetailDebug", "Types récupérés : ${typesList.joinToString()}")
                        updateSpinner()
                    } else {
                        Toast.makeText(this@ForfaitDetailActivity, "Aucun type reçu", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ForfaitDetailActivity, "Erreur de chargement des types", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TypeForfait>>, t: Throwable) {
                Toast.makeText(this@ForfaitDetailActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun updateSpinner() {
        val adapter = object : ArrayAdapter<TypeForfait>(
            this,
            android.R.layout.simple_spinner_item,
            typesList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.text = typesList[position].name // Affiche bien le nom
                view.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.text = typesList[position].name
                view.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.neon_pink))
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypes.adapter = adapter
    }
}
