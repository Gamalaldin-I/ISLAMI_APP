package com.example.readquran.repo.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.readquran.model.FavSuraTrack

@Dao
interface FavTrackDao {
    @Insert
    suspend fun addFavTrack(track: FavSuraTrack)
    @Query("SELECT * FROM FavSuraTrack")
    suspend fun getFavTracks(): List<FavSuraTrack>
    @Query("DELETE FROM FavSuraTrack WHERE id = :id")
    suspend fun deleteFavTrack(id: String)
    @Query("SELECT * FROM FavSuraTrack WHERE id = :id")
    suspend fun searchFavTrack(id: String): FavSuraTrack?



}