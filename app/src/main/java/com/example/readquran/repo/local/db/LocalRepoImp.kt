package com.example.readquran.repo.local.db

import android.content.Context
import com.example.readquran.model.Ayah
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.model.FavVerse
import com.example.readquran.model.LastRead
import com.example.readquran.model.Surah

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepoImp(context: Context): LocalRepo {
    private val db = DatabaseBuilder.getInstance(context)

    override suspend fun getAllSurah(): List<Surah> =
        withContext(Dispatchers.IO){
            db.surahDao().getAll()
        }

    override suspend fun insertSurah(surah: Surah) {
        withContext(Dispatchers.IO){
            db.surahDao().insertSurah(surah)
        }
    }

    override suspend fun deleteSurah() {
        withContext(Dispatchers.IO){
            db.surahDao().deleteAll()
        }
    }

    override suspend fun getSize(): Int {
        return db.surahDao().getCountSurah()
        }

    override suspend fun getSurahByNumber(number: Int): Surah =
        withContext(Dispatchers.IO){
            db.surahDao().getSurahByNumber(number)!!
        }

    override suspend fun getAyatBySurahNumber(number: Int): List<Ayah> =
        withContext(Dispatchers.IO){
        db.ayahDao().getVersesBySurahId(number)
     }

    override suspend fun getWholeAyat(): List<Ayah> =
        withContext(Dispatchers.IO){
            db.ayahDao().getAllAyat()
        }


    override suspend fun insertAyah(ayat: Ayah) {
         withContext(Dispatchers.IO){
             db.ayahDao().insertVerses(ayat)
         }
    }

    override suspend fun updateAyah(ayat: Ayah) {
        withContext(Dispatchers.IO){
            db.ayahDao().updateVerse(ayat)
        }
    }

    override suspend fun getAyahById(number: Int): Ayah {
        return db.ayahDao().getAyahById(number)
    }

    override suspend fun insertLastRead(lastRead: LastRead) {
        withContext(Dispatchers.IO){
            db.lastReadDao().insertLastRead(lastRead)
        }
    }

    override suspend fun getLastRead(): List<LastRead> =
         withContext(Dispatchers.IO){
            db.lastReadDao().getAllLastRead()
        }

    override suspend fun deleteLastRead(id: Int) {
        withContext(Dispatchers.IO){
            db.lastReadDao().deleteLastRead(id)
        }
    }

    override suspend fun deleteHistory() {
        withContext(Dispatchers.IO){
            db.lastReadDao().deleteAllLastRead()
        }
    }

    override suspend fun insertFavourite(favourite: FavVerse) {
        withContext(Dispatchers.IO){
            db.favVerseDao().insertVerse(favourite)
        }
    }

    override suspend fun deleteFavourite(id: Int) {
        withContext(Dispatchers.IO){
            db.favVerseDao().deleteFavVerse(id)
        }
    }

    override suspend fun getFavourites(): List<FavVerse> =
        withContext(Dispatchers.IO){
            db.favVerseDao().getAllFavVerses()
        }

    override suspend fun insertTrack(track: FavSuraTrack) {
        db.favTrackDao().addFavTrack(track)
    }

    override suspend fun deleteTrack(id: String) {
        db.favTrackDao().deleteFavTrack(id)
    }

    override suspend fun getTracks(): List<FavSuraTrack> =
        withContext(Dispatchers.IO) {
            db.favTrackDao().getFavTracks()
        }

    override suspend fun searchTrack(id: String): FavSuraTrack? =
        withContext(Dispatchers.IO) {
            db.favTrackDao().searchFavTrack(id)
        }
}



