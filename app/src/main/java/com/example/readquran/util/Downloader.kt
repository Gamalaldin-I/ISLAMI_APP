package com.example.readquran.util
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import java.io.File

object Downloader {
    private val downloading = ArrayList<String>()
    fun downloadTrackMP3FromUrlInTheInnerStorageOfApp(url:String, context: Context,suraName:String,readerName:String) {
        val fileName = "$suraName $readerName.mp3"
        //check if the file is already downloading
        if(isDownLoadingRecently(fileName)&& !checkIfFileExists(context,fileName)){
            Toast.makeText(context, "The file is already downloading", Toast.LENGTH_SHORT).show()
            return
        }
        downloading.add(fileName)
        //check if the file is already downloaded
        if(checkIfFileExists(context,fileName)){
            Toast.makeText(context, "The file is already downloaded", Toast.LENGTH_SHORT).show()
            return
        }
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Downloading $fileName")
            setDescription("Downloading your audio file...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(context, "audioFiles", fileName)
        }
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    fun getDownloadedTrackMP3(context: Context) : List<File> {
        // get the path of the app directory ("audioFiles")
        val appDir = context.getExternalFilesDir("audioFiles")

        // make sure the directory exists and contains files
        return if (appDir != null && appDir.exists()) {
            appDir.listFiles()?.filter { it.isFile }?.toList() ?: emptyList()
        } else {
            emptyList() // if directory is not exists
        }
    }
    fun deleteDownloadedTrackMP3(context: Context,fileName:String) {
        val appDir = context.getExternalFilesDir("audioFiles")
        //check if the file is already downloading or downloaded recently
        if(isDownLoadingRecently(fileName)){
            downloading.remove(fileName)
        }
        val file = File(appDir, fileName)
        file.delete()
    }
    private fun checkIfFileExists(context: Context, fileName:String):Boolean{
        val appDir = context.getExternalFilesDir("audioFiles")
        val file = File(appDir, fileName)
        return file.exists()
    }
    private fun isDownLoadingRecently(fileName:String):Boolean{
        return (downloading.contains(fileName))
    }

}