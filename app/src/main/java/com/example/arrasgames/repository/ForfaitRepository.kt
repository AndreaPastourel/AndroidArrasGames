package com.example.arrasgames.repository

import com.example.arrasgames.model.Forfait
import com.example.arrasgames.model.Reservation
import com.example.arrasgames.model.ReservationRequest
import com.example.arrasgames.model.ReservationResponse
import com.example.arrasgames.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ForfaitRepository {

    private var forfaitsDisponibles: List<Forfait> = listOf()

    // Charger les forfaits depuis l'API
    fun getForfaits(callback: (List<Forfait>) -> Unit) {
        ApiClient.instance.getForfaits().enqueue(object : Callback<List<Forfait>> {
            override fun onResponse(call: Call<List<Forfait>>, response: Response<List<Forfait>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        forfaitsDisponibles = it
                        callback(it) // Renvoie la liste via le callback
                    }
                } else {
                    callback(emptyList()) // Renvoie une liste vide en cas d'erreur
                }
            }

            override fun onFailure(call: Call<List<Forfait>>, t: Throwable) {
                callback(emptyList()) // Renvoie une liste vide si l'API ne répond pas
            }
        })
    }


    fun reserverForfait(packageId: Int, typeId: Int, callback: (Boolean, String?) -> Unit) {
        val token = UserRepository.getToken()
        if (token == null) {
            callback(false, null)
            return
        }

        val request = ReservationRequest(packageId, typeId)

        ApiClient.instance.reserverForfait("Bearer $token", request).enqueue(object : Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                if (response.isSuccessful) {
                    callback(true, response.body()?.code)
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                callback(false, null)
            }
        })
    }

    fun getReservations(callback: (List<Reservation>) -> Unit) {
        val token = UserRepository.getToken()
        if (token == null) {
            callback(emptyList())
            return
        }

        ApiClient.instance.getReservations("Bearer $token").enqueue(object : Callback<List<Reservation>> {
            override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                if (response.isSuccessful) {
                    callback(response.body() ?: emptyList())
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                callback(emptyList())
            }
        })
    }

        // Simuler l'achat d'un forfait (à modifier plus tard pour l'enregistrer en BDD)
    fun acheterForfait(forfait: Forfait) {
        // Ici, tu peux appeler une API pour enregistrer l'achat, si besoin
        println("${forfait.name} acheté !")
    }
}
