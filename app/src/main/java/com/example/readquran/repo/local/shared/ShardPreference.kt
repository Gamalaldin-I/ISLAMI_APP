package com.example.readquran.repo.local.shared

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class ShardPreference (context: Context){
    private val lastSurahPreference: SharedPreferences =context.getSharedPreferences("surahPref",Context.MODE_PRIVATE)
    private val readFontSize: SharedPreferences =context.getSharedPreferences("fontSize",Context.MODE_PRIVATE)
    private val nightMode: SharedPreferences =context.getSharedPreferences("nightMode",Context.MODE_PRIVATE)

    private val editorFontSize=readFontSize.edit()
    private val editor=lastSurahPreference.edit()
    private val editorNightMode=nightMode.edit()

    //for surah and ayah
    @SuppressLint("CommitPrefEdits")
    fun setSurahName(surahName:String,num:Int){
        editor.putString("surahName",surahName)
        editor.putInt("surahNumber",num)
        editor.apply()
    }
    @SuppressLint("CommitPrefEdits")
    fun setAyahNumber(ayahNumber:String){
        editor.putString("ayahNumber",ayahNumber)
        editor.apply()
    }


    fun getSurahNumber():Int{
        return lastSurahPreference.getInt("surahNumber",0)
    }

    fun getSruahName():String{
       return lastSurahPreference.getString("surahName","لم تحدد")!!
    }
    fun getAyahNumber():Int{
         val lastAya=lastSurahPreference.getString("ayahNumber","لم تحدد")!!
        if(lastAya=="لم تحدد"){
            return 0
        }
        return lastAya.toInt()
    }
    //for font size
    fun setFontSize(fontSize:Float){
        editorFontSize.putFloat("fontSize",fontSize)
        editorFontSize.apply()
    }
    fun getFontSize():Float{
        return readFontSize.getFloat("fontSize",16f)
    }
    //for night mode
    fun setNightMode(nightMode:Boolean,fromSettings:Boolean){
        editorNightMode.putBoolean("nightMode",nightMode)
        editorNightMode.putBoolean("fromSettings",fromSettings)
        editorNightMode.apply()
    }
    fun getNightMode():Boolean{
        return nightMode.getBoolean("nightMode",false)
    }
    fun isFromSettings():Boolean{
        return nightMode.getBoolean("fromSettings",false)
    }


}