package com.example.readquran.util.adapterListener

import com.example.readquran.model.DownloadedFile

interface DownloadFilesListener{
    fun onPlay(file: DownloadedFile,position: Int)
    fun onLongClick(file: DownloadedFile,position: Int)
    fun onDeSelect()
    fun isAllSelected(isAllSelected: Boolean)
}