package com.example.readquran.repo.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.readquran.model.Ayah
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.model.FavVerse
import com.example.readquran.model.LastRead
import com.example.readquran.model.Surah
import com.example.readquran.repo.local.db.dao.AyahDao
import com.example.readquran.repo.local.db.dao.FavTrackDao
import com.example.readquran.repo.local.db.dao.FavVerseDao
import com.example.readquran.repo.local.db.dao.LastReadDao
import com.example.readquran.repo.local.db.dao.SurahDao


@Database(entities = [Surah::class, Ayah::class, LastRead::class,FavVerse::class,FavSuraTrack::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun ayahDao(): AyahDao
    abstract fun lastReadDao(): LastReadDao
    abstract fun favVerseDao(): FavVerseDao
    abstract fun favTrackDao(): FavTrackDao
}