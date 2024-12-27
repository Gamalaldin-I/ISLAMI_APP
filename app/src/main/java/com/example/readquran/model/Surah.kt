package com.example.readquran.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surah")
data class Surah
    (
    @PrimaryKey
    val surahNumber: Int,
    val arabicName:String,
    val englishName: String,
    val revelationType: String,
    val numberOfVerses: Int, )
