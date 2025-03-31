package com.example.arrasgames.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reservation(
    val id: Int,
    val package_id: Int,
    val package_name: String, // Nouveau champ
    val type_id: Int,
    val type_name: String,
    val status: String,
    val code: String,
    val created: String

):Parcelable