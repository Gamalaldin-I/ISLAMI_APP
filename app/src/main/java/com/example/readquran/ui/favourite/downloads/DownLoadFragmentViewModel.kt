package com.example.readquran.ui.favourite.downloads

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

class DownLoadFragmentViewModel :ViewModel() {



    // format time in minutes and seconds
    @SuppressLint("DefaultLocale")
    fun formatTime(ms: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}