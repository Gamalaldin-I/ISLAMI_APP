package com.example.readquran.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavSuraTrack(
    @PrimaryKey
    val id: String, // unique identifier for the record of the reader id + sura number
    val url: String,
    val suraName:String,
    val readerName:String,
    var selected:Boolean=false,
    var willRemove:Boolean=false
)
