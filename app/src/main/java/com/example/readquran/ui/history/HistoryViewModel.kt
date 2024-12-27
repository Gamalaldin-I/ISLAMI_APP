package com.example.readquran.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readquran.model.LastRead
import com.example.readquran.repo.local.db.LocalRepoImp
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private val _historyList = MutableLiveData<List<LastRead>>()
    val historyList: LiveData<List<LastRead>> =_historyList
    //val editedHistoryList: LiveData<List<LastRead>> =_historyList



    fun getHistoryList(db: LocalRepoImp){
        viewModelScope.launch {
            _historyList.value=db.getLastRead()
        }
    }
    fun deleteItem(db:LocalRepoImp,id:Int){
        viewModelScope.launch {
            db.deleteLastRead(id)
            _historyList.value=db.getLastRead()
        }
    }
    fun clear(db:LocalRepoImp) {
        viewModelScope.launch {
            db.deleteHistory()
            _historyList.value = db.getLastRead()
        }
    }
}