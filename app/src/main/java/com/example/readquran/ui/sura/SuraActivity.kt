package com.example.readquran.ui.sura

import Dialogs
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.R
import com.example.readquran.databinding.ActivitySuraBinding
import com.example.readquran.model.Ayah
import com.example.readquran.model.FavVerse
import com.example.readquran.model.Surah
import com.example.readquran.quranData.QuranInfo
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.repo.local.shared.ShardPreference
import com.example.readquran.ui.sound.SoundsActivity
import com.example.readquran.ui.sura.share.GetVersesImage
import com.example.readquran.ui.sura.share.ShareFragment
import com.example.readquran.util.adapter.SuraAyatAdapter
import com.example.readquran.util.adapterListener.AyahAdapterListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SuraActivity : AppCompatActivity(), AyahAdapterListener {
    private var nextSurahNumber=0
    private var translate=false
    private lateinit var ayat: List<Ayah>
    private lateinit var pref: ShardPreference
    private lateinit var ayatAdapter: SuraAyatAdapter
    private lateinit var nextSurah: Surah
    lateinit var db: LocalRepoImp
    lateinit var binding: ActivitySuraBinding
    private var surahNumber by Delegates.notNull<Int>()
    private lateinit var sura: Surah
    private lateinit var surahViewModel:SuraViewModel
    private lateinit var sharingFragment: ShareFragment
    private lateinit var sharingImageFragment: GetVersesImage
    private  var textSize: Float = 20f

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //storage
        ayat= listOf()
//sura= Surah(0,"11","","",100)
        //   nextSurah= Surah(0,"","","",0)
        db= LocalRepoImp(this)
        pref= ShardPreference(this)
        textSize=pref.getFontSize()
        //inflate view
        binding=ActivitySuraBinding.inflate(layoutInflater)
        //get data from intent
        val surahNumber=intent.getIntExtra("surahNumber",1)
        val positionY=intent.getIntExtra("position",-1)
        if(surahNumber in 81..114||surahNumber==1) {binding.seekbar.visibility= View.GONE
        }
        //ayatAdapter=SuraAyatAdapter(listOf(),this,positionY)
        //binding.fehresRecyclerView.adapter=ayatAdapter
        //get data from view model
        ayatAdapter= SuraAyatAdapter(ayat,this,positionY,textSize)
        binding.fehresRecyclerView.adapter=ayatAdapter
        surahViewModel= ViewModelProvider(this)[SuraViewModel::class.java]

        surahViewModel.ayahs.observe(this){
            ayat=it
            ayatAdapter.ayatPrepared(ayat)
            scroll(positionY)
            binding.seekbar.max = ayat.size - 1

        }
        surahViewModel.surah.observe(this){
            sura=it
            viewSurahInfo(sura.arabicName)
            ayatAdapter.setSuraInfo(sura)
            this@SuraActivity.surahNumber =sura.surahNumber

            if(sura.surahNumber<114) {
                surahViewModel.getNextSurah(db,sura.surahNumber)
                //if (sura.surahNumber == 114)
                //the adapter will remove the view of the next surah automatic
            }
            surahViewModel.resetPref(pref,sura.arabicName,sura.surahNumber)

        }
        surahViewModel.nextSurah.observe(this){
            nextSurah=it
            ayatAdapter.setNextSuraInfo(nextSurah)
            nextSurahNumber=it.surahNumber
        }
        //get from view Model
        surahViewModel.getSurahByNumber(db,surahNumber)
        surahViewModel.getAyahsBySurahNumber(db,surahNumber)

        //controllers
        binding.backArrow.setOnClickListener{
            finish()
        }






        // Listener for SeekBar changes to control scrolling
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // Scroll the RecyclerView to the item at the SeekBar position
                    (binding.fehresRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(progress, 0)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.fehresRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                binding.seekbar.progress = firstVisibleItemPosition
                if (lastVisibleItemPosition==binding.fehresRecyclerView.adapter?.itemCount?.minus(1)) {
                    binding.seekbar.animate().setStartDelay(0).scaleX(20f).withEndAction {
                        binding.seekbar.progress = binding.seekbar.max

                    }.setDuration(500).start()
                }
                else{
                    binding.seekbar.animate().scaleX(1f).setDuration(0).start()
                }
            }
        })


        ayatAdapter= SuraAyatAdapter(ayat,this,positionY,textSize)
        binding.fehresRecyclerView.adapter=ayatAdapter

        binding.translationBtn.setOnClickListener{
            binding.translationBtn.animate().rotationBy(180f).setDuration(250).start()
            translate=!translate
            ayatAdapter.translate(translate)
        }
        binding.shareBtn.setOnClickListener{
            var sharedTxt=sharingFragment.getSharedText()
            when(sharingFragment.getSelectedId()){

                R.id.textRB->{
                    sharedTxt+=sura.arabicName
                    val shareIntent= Intent(Intent.ACTION_SEND)
                    shareIntent.type="text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT,sharedTxt)
                    startActivity(Intent.createChooser(shareIntent,"Share via"))
                }
                R.id.imageRB->{
                    // From the current fragment
                    sharingImageFragment= GetVersesImage().newInstance(sharedTxt,sura.arabicName)
                    setSharingFragment(sharingImageFragment)

                }
            }
            //hide share container
            //this handeler to syncronise with the share image fragment
            Handler().postDelayed({
                animateCancelTheFrameLayout()

            },700)
        }
        binding.hideBtn.setOnClickListener {
            binding.hideBtn.animate().rotationBy(180f).setDuration(250).start()
            animateCancelTheFrameLayout()
        }

        setContentView(binding.root)

    }
    //set surah View info
    private fun viewSurahInfo(suraName:String){
        binding.suraName.text=suraName
    }


    override fun getTafseer(ayah: Ayah, position: Int) {
        Dialogs.showTafserDialog(this,ayah.tafser,sura.arabicName,ayah.numberInSurah)
    }

    override fun share(ayah: Ayah, position: Int) {
        sharingFragment = ShareFragment().getInstance(ayah.numberInSurah,sura)
        setSharingFragment(sharingFragment)

    }

    override fun saveLastRead(ayaNumber: Int) {
        surahViewModel.setPref(pref,sura.arabicName,sura.surahNumber,ayaNumber)
    }



    override fun goToNextSura(surahNumber: Int) {
        val intent2= Intent(this, SuraActivity::class.java)
        intent2.putExtra("surahNumber",surahNumber)
        startActivity(intent2)
        this.finish()}

    override fun playSuraVoice() {
        val intent= Intent(this, SoundsActivity::class.java)
        intent.putExtra("suraName",sura.arabicName)
        intent.putExtra("indexOfSura",sura.surahNumber-1)
        startActivity(intent)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun setAyaFavourite(ayah: Ayah, position: Int) {
          val favouriteAya=FavVerse(ayah.number,ayah.text,ayah.numberInSurah,QuranInfo.map[ayah.surahId]!!,ayah.surahId)
            GlobalScope.launch(Dispatchers.IO){
                db.insertFavourite(favouriteAya)
                //this line below to prevent the auto selection after clicking on favourite>>
                if(ayah.selected){
                    ayah.selected=false
                    ayah.saved=false
                }
                db.updateAyah(ayah)
                ayah.selected=true
            }
        }

    @OptIn(DelicateCoroutinesApi::class)
    override fun removeAyaFavourite(ayah: Ayah, position: Int) {
        GlobalScope.launch(Dispatchers.IO){
            db.deleteFavourite(ayah.number)
            //this line below to prevent the auto selection after clicking on favourite>>
            if(ayah.selected){
                ayah.selected=false
                ayah.saved=false
            }
            db.updateAyah(ayah)
            ayah.selected=true
        }

    }

    private fun scroll(i:Int){
        if(i<0) return
        binding.fehresRecyclerView.post {
            // Delay to ensure layout is rendered
            binding.fehresRecyclerView.scrollToPosition(i)
        }
    }
    private fun setSharingFragment(sharingFragment: Fragment) {
        animateOpenTheFrameLayout()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLo, sharingFragment)
        fragmentTransaction.commit()
    }

    override fun onStop() {
        super.onStop()
        val ayat=ayatAdapter.savedAyas
        if(ayat.isNotEmpty()){
            surahViewModel.addToHistory(db,sura,ayat)
        }
    }
    private fun animateOpenTheFrameLayout(){
        binding.shareContainer.visibility=View.VISIBLE
        binding.shareContainer.alpha=0f
        binding.shareContainer.animate().scaleY(0f).scaleX(0f).setDuration(0).start()

        binding.shareContainer.animate().scaleY(1f).scaleX(1f).alpha(1f).setDuration(500).start()
    }

    private fun animateCancelTheFrameLayout(){
        binding.shareContainer.animate().scaleX(0f).scaleY(0f).setDuration(300).withEndAction {
           // binding.shareContainer.animate().scaleX(1f).scaleY(1f).setDuration(0).start()
            binding.shareContainer.visibility = View.GONE
        }.start()
    }


}