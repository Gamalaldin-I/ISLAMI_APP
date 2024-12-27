package com.example.readquran.util.adapterListener

import com.example.readquran.model.Surah

interface FehresAdapterListener {
    fun surahOnClicked(surah: Surah, position: Int)
}