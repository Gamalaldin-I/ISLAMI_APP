package com.example.readquran.model

data class DownloadedFile( val fileName: String,
                           val filePath: String,
                           var selected: Boolean = false,
                           var appear: Boolean = false
)