package com.example.readquran.repo.remote.interf

import com.example.readquran.repo.remote.response.JsonOb
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface QuranVoiceApi {
    @GET("{reader}")
    suspend fun getReaderVoice(
        @Path("reader") reader:Int): Response<JsonOb>


}