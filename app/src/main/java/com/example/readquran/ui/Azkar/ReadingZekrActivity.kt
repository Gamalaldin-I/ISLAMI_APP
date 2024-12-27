package com.example.readquran.ui.Azkar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.readquran.databinding.ActivityReadingZekrBinding
import com.example.readquran.jsonParser.AllAzkar
import com.example.readquran.util.adapter.AzkarAdapter
import com.google.gson.Gson

class ReadingZekrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadingZekrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingZekrBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        val azkar = getAzkar()
        val azkarId= intent.getIntExtra("azkarId",0)
        val azkarList = when (azkarId) {
            1 -> azkar.afterSalah
            2 -> azkar.wakeup
            3 -> azkar.evening
            4 -> azkar.morning
            5 -> azkar.napiDoa
            6 -> azkar.quranDoa
            7 -> azkar.sleeping
            8 -> azkar.tasbeeh
            else -> emptyList()
        }
        Toast.makeText(this, "${azkarList.size}", Toast.LENGTH_SHORT).show()
        binding.azkarAdapter.adapter = AzkarAdapter(azkarList)
        setContentView(binding.root)



    }

        private fun getAzkar(): AllAzkar {
            val inputStream=assets.open("azkar.json")
            val jsonStr=inputStream.bufferedReader().use { it.readText() }
            val azkar= Gson().fromJson(jsonStr, AllAzkar::class.java)
            inputStream.close()
            return azkar
        }



}