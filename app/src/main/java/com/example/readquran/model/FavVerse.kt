package com.example.readquran.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "fav_verse")
data class FavVerse(
    @PrimaryKey
    val id:Int,
    val text:String,
    val number:Int,
    val suraName:String,
    val suraId:Int,
    var willBeRemoved:Boolean=false
)