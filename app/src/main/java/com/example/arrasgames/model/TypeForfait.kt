package com.example.arrasgames.model

import com.google.gson.annotations.SerializedName

data class TypeForfait(
    @SerializedName("id") val type_id: Int,
    @SerializedName("name") val name: String
)