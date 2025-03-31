package com.example.arrasgames.repository

import LoginRequest
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.arrasgames.model.LoginResponse
import com.example.arrasgames.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }



    private fun saveToken(token: String?) {
        sharedPreferences.edit().putString("api_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("api_token", null)
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        val request = LoginRequest(email, password)

        ApiClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LoginDebug", "Code HTTP : ${response.code()}")
                Log.d("LoginDebug", "Réponse brute : ${response.errorBody()?.string()}")

                if (response.isSuccessful && response.body()?.success == true) {
                    val token = response.body()?.api_token
                    saveToken(token)
                    callback(true, token)
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginDebug", "Erreur de connexion : ${t.message}")
                callback(false, null)
            }
        })
    }

}
