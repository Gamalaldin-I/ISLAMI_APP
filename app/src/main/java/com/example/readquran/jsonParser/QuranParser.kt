package com.example.readquran.jsonParser
data class Quran (
    val quran: ArrayList<Sura>
)
data class Sura(
    val id:Int,
    val name:String,
    val englishName:String,
    val type:String,
    val versesNumber:Int,
    val verses:ArrayList<Aya>
)
data class Aya(
    var numberInSura:Int,
    val numberInQuran: Int,
    var arabicContent:String,
    val englishContent:String,
    val tafser:String,
    val page:Int,
    val juz:Int
)