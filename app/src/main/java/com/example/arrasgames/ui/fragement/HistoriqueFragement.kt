package com.example.arrasgames.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arrasgames.R
import com.example.arrasgames.model.Reservation
import com.example.arrasgames.network.ApiClient
import com.example.arrasgames.ui.adapter.HistoriqueAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoriqueFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoriqueAdapter
    private lateinit var progressBar: ProgressBar
    private val reservationsList = mutableListOf<Reservation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_historique, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewHistorique)
        progressBar = view.findViewById(R.id.progressBar)

        adapter = HistoriqueAdapter(reservationsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadReservations()
    }

    private fun loadReservations() {
        progressBar.visibility = View.VISIBLE

        // Récupérer le token API depuis SharedPreferences
        val apiToken = requireContext()
            .getSharedPreferences("user_prefs", 0)
            .getString("api_token", "") ?: ""

        if (apiToken.isEmpty()) {
            Toast.makeText(requireContext(), "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            return
        }

        Log.d("HistoriqueDebug", "Envoi de la requête avec token: $apiToken")

        // Envoi de la requête avec le token d'authentification
        ApiClient.instance.getReservations("Bearer $apiToken").enqueue(object : Callback<List<Reservation>> {
            override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val reservations = response.body()!!
                    Log.d("HistoriqueDebug", "Nombre de réservations reçues : ${reservations.size}")

                    reservationsList.clear()
                    reservationsList.addAll(reservations)
                    adapter.notifyDataSetChanged()

                    // Log chaque réservation reçue
                    reservations.forEachIndexed { index, res ->
                        Log.d("HistoriqueDebug", "Réservation ${index + 1} - Package: ${res.package_name}, Code: ${res.code}, Statut: ${res.status}")
                    }
                } else {
                    Toast.makeText(requireContext(), "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("HistoriqueDebug", "Réponse: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show()
                Log.e("HistoriqueDebug", "Erreur: ${t.message}")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadReservations() // Recharger les réservations à chaque retour sur le fragment
    }
}
