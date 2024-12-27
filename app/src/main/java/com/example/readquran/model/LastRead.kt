package com.example.readquran.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "last_read")
data class LastRead(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val suraName: String,
    val suraNumber: Int,
    val ayahNumber: Int,
)