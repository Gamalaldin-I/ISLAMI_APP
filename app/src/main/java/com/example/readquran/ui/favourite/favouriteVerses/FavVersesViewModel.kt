package com.example.readquran.ui.favourite.favouriteVerses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.readquran.model.FavVerse
import com.example.readquran.repo.local.db.LocalRepoImp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavVersesViewModel:ViewModel(){
    private var _favVerses: MutableLiveData<List<FavVerse>> = MutableLiveData()
    var favVerses: LiveData<List<FavVerse>> = _favVerses

    @OptIn(DelicateCoroutinesApi::class)
    fun getFavVerses(db:LocalRepoImp){
        GlobalScope.launch(Dispatchers.IO){
            _favVerses.postValue(db.getFavourites())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteFavVerse(verse: ArrayList<FavVerse>, db:LocalRepoImp){
        GlobalScope.launch(Dispatchers.IO){
            for (v in verse){
                db.deleteFavourite(v.id)
            }
        }


    }

}