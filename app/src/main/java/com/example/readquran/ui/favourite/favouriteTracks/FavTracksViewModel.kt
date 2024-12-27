package com.example.readquran.ui.favourite.favouriteTracks

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.repo.local.db.LocalRepoImp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class FavTracksViewModel:ViewModel() {

    private var _favTracks :MutableLiveData<List<FavSuraTrack>> = MutableLiveData<List<FavSuraTrack>>()
    var favTracks : LiveData<List<FavSuraTrack>> = _favTracks

    fun getFavTracks(db:LocalRepoImp){
        viewModelScope.launch (Dispatchers.IO){
            val favRecords = db.getTracks()
            _favTracks.postValue(favRecords)
        }
    }
    @SuppressLint("DefaultLocale")
     fun formatTime(ms: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun removeRecords(records:ArrayList<FavSuraTrack>,db:LocalRepoImp){
        if(records.isNotEmpty()){
            GlobalScope.launch (Dispatchers.IO){
                for(record in records){
                    db.deleteTrack(record.id)
                }
            }
        }
    }


}