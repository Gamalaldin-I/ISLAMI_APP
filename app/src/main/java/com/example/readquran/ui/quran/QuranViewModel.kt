package com.example.readquran.ui.quran

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readquran.model.Ayah
import com.example.readquran.model.Surah
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.repo.local.shared.ShardPreference
import kotlinx.coroutines.launch

class QuranViewModel : ViewModel() {
    private var _quranList: MutableLiveData<List<Surah>> = MutableLiveData<List<Surah>>()
    private var _ayahList: MutableLiveData<List<Ayah>> = MutableLiveData<List<Ayah>>()
    var quranList: LiveData<List<Surah>> = _quranList
    var ayahList: LiveData<List<Ayah>> = _ayahList

    fun getQuranList(repo: LocalRepoImp){
        viewModelScope.launch {
            _quranList.value= repo.getAllSurah()
        }
    }
    fun getWholeAyas(repo: LocalRepoImp){
        viewModelScope.launch {
            _ayahList.value=repo.getWholeAyat()
        }
    }
    fun getLastSuraInfo(pref: ShardPreference):Pair<String,Int>{
        return Pair(pref.getSruahName(),pref.getAyahNumber())
    }
    fun setLastSuraInfo(pref: ShardPreference, intent: Intent){
        intent.putExtra("position",pref.getAyahNumber())
        intent.putExtra("surahNumber",pref.getSurahNumber())
    }
    fun filterSuraSearch(text:String, list:List<Surah>):List<Surah>{
        val filtered= mutableListOf<Surah>()
        for (item in list){
            if (item.arabicName.contains(text, ignoreCase = true)){
                filtered.add(item)
            }
        }
        return filtered
    }
    fun filterAyaSearch(text:String, list:List<Ayah>):List<Ayah>{
        val filtered= mutableListOf<Ayah>()
        for (item in list){
            if (item.searchTxt.contains(text)){
                filtered.add(item)
            }
        }
        return filtered
    }
}
