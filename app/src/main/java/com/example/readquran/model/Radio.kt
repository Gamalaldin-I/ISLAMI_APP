package com.example.readquran.model

import com.google.gson.annotations.SerializedName

data class Radio(
     @SerializedName("id") var id: Int,
     @SerializedName("name") var name: String,
     @SerializedName("url") var url: String,
     @SerializedName("img") var img: String
)

