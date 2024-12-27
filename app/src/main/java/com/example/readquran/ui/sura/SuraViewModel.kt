package com.example.readquran.ui.sura

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readquran.model.Ayah
import com.example.readquran.model.LastRead
import com.example.readquran.model.Surah
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.repo.local.shared.ShardPreference
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SuraViewModel : ViewModel() {
    private val _surah = MutableLiveData<Surah>()
    val surah: LiveData<Surah> = _surah
    private val _nextSurah = MutableLiveData<Surah>()
    val nextSurah: LiveData<Surah> = _nextSurah
    private val _ayahs = MutableLiveData<List<Ayah>>()
    val ayahs: LiveData<List<Ayah>> = _ayahs

    fun getSurahByNumber(repo: LocalRepoImp, surahNumber: Int) {
        viewModelScope.launch {
            val surah = repo.getSurahByNumber(surahNumber)
            _surah.value =surah
        }
    }
    fun getNextSurah(repo:LocalRepoImp,surahNumber: Int){
        viewModelScope.launch {
            val nextSurah = repo.getSurahByNumber(surahNumber+1)
            _nextSurah.value = nextSurah
        }
    }

    fun getAyahsBySurahNumber(repo:LocalRepoImp,surahNumber: Int){
        viewModelScope.launch {
            val ayahs = repo.getAyatBySurahNumber(surahNumber)
            _ayahs.value = ayahs
        }
    }
    fun resetPref(pref: ShardPreference,suraName:String,num:Int){
        pref.setAyahNumber("لم تحدد")
        pref.setSurahName(suraName,num)

    }
    fun setPref(pref: ShardPreference, suraName:String, suraNumber:Int, ayahNumber:Int){
        pref.setSurahName(suraName,suraNumber)
        pref.setAyahNumber(ayahNumber.toString())
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun addToHistory(db:LocalRepoImp, sura:Surah, savedAyat:ArrayList<Int>){
        GlobalScope.launch{
            for(ayaNumber in savedAyat){
                db.insertLastRead(lastRead = LastRead(0,sura.arabicName,sura.surahNumber,ayaNumber))
            }

        }
    }

}