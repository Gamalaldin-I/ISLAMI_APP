package com.example.readquran.jsonParser

import com.google.gson.annotations.SerializedName

data class AllAzkar(
    @SerializedName("أذكار الصباح")
    val morning: List<ZekrParser>,
    @SerializedName("أذكار المساء")
    val evening: List<ZekrParser>,
    @SerializedName("أذكار بعد السلام من الصلاة المفروضة")
    val afterSalah: List<ZekrParser>,
    @SerializedName("تسابيح")
    val tasbeeh: List<ZekrParser>,
    @SerializedName("أذكار النوم")
    val sleeping: List<ZekrParser>,
    @SerializedName("أذكار الاستيقاظ")
    val wakeup: List<ZekrParser>,
    @SerializedName("أدعية قرآنية")
    val quranDoa: List<ZekrParser>,
    @SerializedName("أدعية الأنبياء")
    val napiDoa: List<ZekrParser>
)



data class ZekrParser(
    val category:String,
    val count:String,
    val description:String,
    val reference:String,
    val content:String
)
