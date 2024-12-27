package com.example.readquran.repo.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.readquran.model.FavVerse
import com.example.readquran.model.LastRead
@Dao
interface FavVerseDao {
    @Query("SELECT * FROM fav_verse ORDER BY id DESC")
    suspend fun getAllFavVerses(): List<FavVerse>
    @Insert
    suspend fun insertVerse(fav: FavVerse)
    @Query("DELETE FROM fav_verse WHERE id = :id")
    suspend fun deleteFavVerse(id: Int)

}