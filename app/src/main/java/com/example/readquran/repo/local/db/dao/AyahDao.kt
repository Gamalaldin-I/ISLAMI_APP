package com.example.readquran.repo.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.readquran.model.Ayah

@Dao
interface AyahDao
{
    @Query("SELECT * FROM ayah WHERE surahId = :surahId ORDER BY number ASC " )
    suspend fun getVersesBySurahId(surahId:Int):List<Ayah>
    @Query("SELECT * FROM ayah")
    suspend fun getAllAyat():List<Ayah>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verse: Ayah)
    @Update
    suspend fun updateVerse(verse: Ayah)
    @Query("Select * FROM ayah WHERE number = :id")
    suspend fun getAyahById(id:Int):Ayah


}