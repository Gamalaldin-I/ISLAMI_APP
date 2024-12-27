package com.example.readquran.repo.local.db

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: LocalDatabase?=null
    fun getInstance(context: Context): LocalDatabase {
        if (INSTANCE == null) {
            synchronized(LocalDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java, "Quran_database"
                    ).build()
                }
            }
        }
        return INSTANCE!!
    }
}