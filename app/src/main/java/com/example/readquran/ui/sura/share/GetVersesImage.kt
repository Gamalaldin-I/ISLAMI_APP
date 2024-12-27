package com.example.readquran.ui.sura.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.readquran.databinding.FragmentGetVersesImageBinding
import java.io.File
import java.io.FileOutputStream

class GetVersesImage : Fragment() {
private lateinit var binding: FragmentGetVersesImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    fun newInstance(sharedTxt: String,surahName:String): GetVersesImage {
        val args = Bundle()
        args.putString("sharedTxt", sharedTxt)
        args.putString("surahName", surahName)
        val fragment = GetVersesImage()
        fragment.arguments = args
        return fragment
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGetVersesImageBinding.inflate(inflater, container, false)
        val sharedTxt = arguments?.getString("sharedTxt")
        val surahName=arguments?.getString("surahName")

        binding.quranTxt.text=sharedTxt
        binding.suraName.text=surahName

        Handler().postDelayed({
            val bitmap = getBitmapFromView(binding.LayoutImage)
            val file=saveBitmapToCache(bitmap,requireContext())
            shareImage(file,requireContext())
        },500)

        return binding.root
    }
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    fun saveBitmapToCache(bitmap: Bitmap, context: Context): File {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, "shared_image.png")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return file
    }
    private fun shareImage(file: File, context: Context) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }




}