package com.example.readquran.repo.remote.interf

import com.example.readquran.repo.remote.response.RadioResponse
import retrofit2.Response
import retrofit2.http.GET

interface RadioApi {
    @GET("/radio.json")
    suspend fun getRadioStations(): Response<RadioResponse>
}