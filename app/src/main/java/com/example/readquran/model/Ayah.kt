package com.example.readquran.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "ayah")
data class Ayah(
    @PrimaryKey
    val number:Int,
    val surahId: Int,
    val numberInSurah : Int,
    var selected: Boolean,
    var saved: Boolean,
    var favourite: Boolean,
    var text :String,
    val searchTxt : String,
    val translationText : String,
    val tafser:String,
)
