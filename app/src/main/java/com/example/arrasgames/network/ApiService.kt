package com.example.arrasgames.network

import LoginRequest
import com.example.arrasgames.model.Forfait
import com.example.arrasgames.model.LoginResponse
import com.example.arrasgames.model.Reservation
import com.example.arrasgames.model.ReservationRequest
import com.example.arrasgames.model.ReservationResponse
import com.example.arrasgames.model.TypeForfait
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("index.php?endpoint=forfaits")
    fun getForfaits(): Call<List<Forfait>>

    @GET("index.php?endpoint=types")
    fun getTypes(): Call<List<TypeForfait>>

    @POST("index.php?endpoint=reservations")
    @Headers("Content-Type: application/json")
    fun reserverForfait(
        @Header("Authorization") token: String,
        @Body reservation: ReservationRequest
    ): Call<ReservationResponse>

    @GET("index.php?endpoint=reservations")
    fun getReservations(@Header("Authorization") token: String): Call<List<Reservation>>


    @POST("index.php?endpoint=login")
    @Headers("Content-Type: application/json")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}

