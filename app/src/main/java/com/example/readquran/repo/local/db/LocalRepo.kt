package com.example.readquran.repo.local.db

import com.example.readquran.model.Ayah
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.model.FavVerse
import com.example.readquran.model.LastRead
import com.example.readquran.model.Surah

interface LocalRepo {
    //sura
    suspend fun getAllSurah(): List<Surah>
    suspend fun insertSurah(surah: Surah)
    suspend fun deleteSurah()
    suspend fun getSize():Int
    suspend fun getSurahByNumber(number:Int): Surah
    //ayat
    suspend fun getAyatBySurahNumber(number: Int):List<Ayah>
    suspend fun getWholeAyat():List<Ayah>
    suspend fun insertAyah(ayat: Ayah)
    suspend fun updateAyah(ayat: Ayah)
    suspend fun getAyahById(number: Int):Ayah
    //history(last read)
    suspend fun insertLastRead(lastRead: LastRead)
    suspend fun getLastRead():List<LastRead>
    suspend fun deleteLastRead(id: Int)
    suspend fun deleteHistory()
    //favourite verses
    suspend fun insertFavourite(favourite:FavVerse)
    suspend fun deleteFavourite(id: Int)
    suspend fun getFavourites():List<FavVerse>
    //favourite tracks
    suspend fun insertTrack(track:FavSuraTrack)
    suspend fun deleteTrack(id: String)
    suspend fun getTracks():List<FavSuraTrack>
    suspend fun searchTrack(id: String):FavSuraTrack?





}