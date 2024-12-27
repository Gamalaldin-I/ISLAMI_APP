package com.example.readquran.util.adapterListener

import com.example.readquran.model.Ayah


interface AyahAdapterListener {
    fun getTafseer(ayah: Ayah, position: Int)
    fun share(ayah: Ayah, position: Int)
    fun saveLastRead(ayaNumber: Int)
    fun goToNextSura(surahNumber: Int)
    fun playSuraVoice()
    fun setAyaFavourite(ayah: Ayah, position: Int)
    fun removeAyaFavourite(ayah: Ayah, position: Int)
}
