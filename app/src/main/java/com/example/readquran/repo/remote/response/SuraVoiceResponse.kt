package com.example.readquran.repo.remote.response

data class JsonOb(
    val audio_files:List<SuraVoice>
)
data class SuraVoice(
    val id:Int,
    val chapter_id:Int,
    val file_size:Int,
    val format:String,
    val audio_url:String
)