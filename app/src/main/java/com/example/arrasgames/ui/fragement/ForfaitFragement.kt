package com.example.arrasgames.ui.fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arrasgames.R
import com.example.arrasgames.model.Forfait
import com.example.arrasgames.network.ApiClient
import com.example.arrasgames.repository.ForfaitRepository
import com.example.arrasgames.ui.adapter.ForfaitsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForfaitsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ForfaitsAdapter
    private val forfaitsList = mutableListOf<Forfait>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forfaits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewForfaits)
        adapter = ForfaitsAdapter(forfaitsList) { forfait ->
            ForfaitRepository.reserverForfait(forfait.id, 1) { success, code ->
                if (success) {
                    Toast.makeText(requireContext(), "Réservé ! Code : $code", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la réservation", Toast.LENGTH_SHORT).show()
                }
            }
        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Charger les forfaits depuis l'API
        ForfaitRepository.getForfaits { forfaits ->
            if (forfaits.isNotEmpty()) {
                adapter.updateList(forfaits) // Mettre à jour la RecyclerView
            }
        }

    }




    fun onFailure(call: Call<List<Forfait>>, t: Throwable) {
        Toast.makeText(requireContext(), "Erreur de chargement", Toast.LENGTH_SHORT).show()
    }
}
