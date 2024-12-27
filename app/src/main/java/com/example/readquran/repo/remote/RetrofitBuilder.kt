package com.example.readquran.repo.remote

import com.example.readquran.repo.remote.interf.QuranVoiceApi
import com.example.readquran.repo.remote.interf.RadioApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private var readersRetrofit : Retrofit? = null
    private var radioRetrofit : Retrofit? = null

    private const val readersUrl = "https://api.quran.com/api/v4/chapter_recitations/"
    private const val radioUrl="https://data-rosy.vercel.app"


    private fun getInstance(req:Int,url:String) : Retrofit? {
            val gson: Gson = GsonBuilder()
                .create()
             var retrofit =when (req) {
                 1-> readersRetrofit
                 2-> radioRetrofit
                 else -> null
             }
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
    }
        return retrofit!!
    }
    val quranVoiceApi: QuranVoiceApi by lazy {
        getInstance(1, readersUrl)!!.create(QuranVoiceApi::class.java)
    }
    val radioApi: RadioApi by lazy {
        getInstance(2, radioUrl)!!.create(RadioApi::class.java)
    }

}