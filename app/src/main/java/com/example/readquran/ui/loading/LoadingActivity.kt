package com.example.readquran.ui.loading

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.readquran.databinding.ActivityLoadingBinding
import com.example.readquran.jsonParser.Aya
import com.example.readquran.jsonParser.Quran
import com.example.readquran.model.Ayah
import com.example.readquran.model.Surah
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.ui.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class LoadingActivity : AppCompatActivity() {
    private var counter = 0.00f
    //database
    private lateinit var db: LocalRepoImp
    private lateinit var quran: Quran
    //binding
    private lateinit var binding: ActivityLoadingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = LocalRepoImp(this)
        quran=getAQuran()

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        runBlocking {
            loadingData(counter.toInt())}

        setContentView(binding.root)

    }

    private suspend fun loadingData(begin:Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            //try {

            //insert data to database
            for (i in begin..113) {
                // loop on each sura in quran
                val surah=quran.quran[i]
                //prepare empty list of verses
                // loop on each ayah in each sura in english and arabic
                if (surah.id != 9 && surah.id != 1) {
                    surah.verses[0].arabicContent=removeBasmallahInArabic(surah.verses[0])
                }
                for (o in 0..<surah.versesNumber) {
                    val verse=surah.verses[o]
                    db.insertAyah(
                        Ayah(
                            verse.numberInQuran,
                            surah.id,
                            verse.numberInSura,
                            false,
                            saved = false,
                            favourite = false,
                            text = verse.arabicContent,
                            searchTxt = removeTashkeel(verse.arabicContent),
                            translationText = verse.englishContent,
                            tafser = verse.tafser
                        )
                    )
                }

                //insert sura to database
                db.insertSurah(
                    Surah(
                        surah.id,
                        surah.name,
                        surah.englishName,
                        surah.type,
                        surah.versesNumber
                    )
                )
                counter++

                withContext(Dispatchers.Main) {
                    updateUi()
                }
            }
            withContext(Dispatchers.Main) {
                startActivity( Intent(this@LoadingActivity, MainActivity::class.java))}

        }
    }

    private fun updateUi() {
        binding.ncom.text = counter.toInt().toString()
        binding.progressBar.progress= ((counter / 114.00) * 100).toInt()
    }
    //remove basmallah from the arabic text
    private fun removeBasmallahInArabic(ayah: Aya): String {
        return ayah.arabicContent.substring(39)
    }


    private fun getAQuran(): Quran {
        val inputStream=assets.open("wholeQuran.json")
        val inputStreamReader= InputStreamReader(inputStream)
        val quran= Gson().fromJson(inputStreamReader, Quran::class.java)
        inputStreamReader.close()
        inputStream.close()
        return quran
    }



    private fun removeTashkeel(text: String): String {
        return text.replace(Regex("[\\u0610-\\u061A\\u064B-\\u0652]"), "").replace("ٰ","ا").replace("ٱ","ا").replace("ٓ","").replace("ۡ","")
    }
}
