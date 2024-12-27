package com.example.readquran.util

object ArabicTranslator {
    fun toArabicNumerals(number: Int): String {
        val arabicNumerals = "٠١٢٣٤٥٦٧٨٩"
        return number.toString().map { arabicNumerals[it - '0'] }.joinToString("")
    }
    fun meccanOrMedinan(type: String): String {
        if (type == "Medinan") return "مدنية "
        else return "مكية "
    }
}