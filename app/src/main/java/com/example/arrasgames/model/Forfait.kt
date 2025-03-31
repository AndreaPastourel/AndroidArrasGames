package com.example.arrasgames.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forfait(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double
) : Parcelable
