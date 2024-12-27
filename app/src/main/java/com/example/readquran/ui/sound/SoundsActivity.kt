package com.example.readquran.ui.sound

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.readquran.R
import com.example.readquran.databinding.ActivitySoundsBinding
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.model.Reader
import com.example.readquran.repo.local.Readers
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.repo.remote.RetrofitBuilder
import com.example.readquran.repo.remote.response.SuraVoice
import com.example.readquran.util.Animator
import com.example.readquran.util.Animator.animateBtn
import com.example.readquran.util.Downloader
import com.example.readquran.util.WifiBroadcastReceiver
import com.example.readquran.util.adapter.ReadersAdapter
import com.example.readquran.util.adapterListener.ReaderListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class SoundsActivity : AppCompatActivity(), ReaderListener {

    private lateinit var binding: ActivitySoundsBinding
    private lateinit var exoPlayer: ExoPlayer
    private var indexOfSura by Delegates.notNull<Int>()
    private lateinit var suraName: String
    private lateinit var suraVoice: SuraVoice
    private var isPlaying = false
    private lateinit var runningVoiceUrl:String
    private lateinit var currentReader: Reader
    private lateinit var wifiRc:WifiBroadcastReceiver
    private lateinit var adapter: ReadersAdapter
    private lateinit var db:LocalRepoImp

    private val handler = Handler(Looper.getMainLooper())
    private var isSeeking = false
    private var isFavorite:Boolean=false
    private var cardIsVisible=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db=LocalRepoImp(this)

        // Inflate binding
        binding = ActivitySoundsBinding.inflate(layoutInflater)
        binding.soundPlayerCard.visibility=GONE
        binding.closeBtn.setOnClickListener {
            Animator.animateClosingCardOfExoPlayer(binding.soundPlayerCard)
            cardIsVisible=false
            stopAudio()
        }
        wifiRc= WifiBroadcastReceiver({ onConnToWiFi() }, {onDisConnToWiFi()})
        //to register the receiver and keep look at wifi status
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiRc,filter)



        setContentView(binding.root)
        //search
        binding.searchView2.setOnQueryTextListener(object :android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                val filtered=filterReaders(text!!,Readers.readers)
                adapter=ReadersAdapter(filtered,this@SoundsActivity)
                binding.recyclerView.adapter=adapter
                return false
            }

        } )

        // Extract intent extras
        suraName = intent.getStringExtra("suraName") ?: "Unknown Sura"
        indexOfSura = intent.getIntExtra("indexOfSura", 0)
        binding.suraName.text = suraName
        //setAsFavTRack
        binding.favButton.setOnClickListener {
            isFavorite=!isFavorite
            //check if it is favorite now i will add it to the fav list
            val currentId="${indexOfSura+1}${currentReader.id}"
            if (isFavorite){
                animateBtn(binding.favButton)
                binding.favButton.setImageResource(R.drawable.fav_fill)
                val newRecord=FavSuraTrack(currentId,runningVoiceUrl,suraName,currentReader.name)
                addFavTrack(newRecord)
            }
            else{
                binding.favButton.setImageResource(R.drawable.fav_out)
                removeTrack(currentId)
            }

        }
        binding.downLoadBtn.setOnClickListener {
            animateBtn(binding.downLoadBtn)
            Downloader.downloadTrackMP3FromUrlInTheInnerStorageOfApp(runningVoiceUrl,
                this,
                suraName,
                currentReader.name)
        }



        // Setup UI and ExoPlayer
        setupRecyclerView()
        setupExoPlayer()
        setupControls()
        // Update UI
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_READY) {
                    val durationMs = exoPlayer.duration
                    binding.seekBar.max = durationMs.toInt()
                    binding.duration.text = formatTime(durationMs)
                    updateSeekBar()
                }
            }
        })

        // SeekBar change listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.currentTime.text = formatTime(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                exoPlayer.seekTo(seekBar?.progress?.toLong() ?: 0)
            }
        })
    }

    /**
     * Setup the RecyclerView for the list of readers.
     */
    private fun setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        adapter = ReadersAdapter(Readers.readers, this)
        binding.recyclerView.adapter = adapter
    }

    /**
     * Initialize ExoPlayer.
     */
    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    /**
     * Set up control buttons for ExoPlayer.
     */
    private fun setupControls() {
        binding.playPauseBtn.setOnClickListener {playAudio()}
        binding.backArrow.setOnClickListener { finish() }
    }

    /**
     * Handle the click event on a reader in the RecyclerView.
     */
    override fun onClick(reader: Reader, position: Int) {
        currentReader=reader
        binding.readerNameTV.text=reader.name

        // Stop any currently playing audio
        stopAudio()

        // Fetch the audio URL for the selected reader
        fetchReaderVoice(reader.id)

    }

    /**
     * Fetch the voice of the selected reader using Retrofit.
     */
    private fun fetchReaderVoice(readerId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitBuilder.quranVoiceApi.getReaderVoice(readerId)
                if (response.isSuccessful) {
                    //sura voice
                    suraVoice = response.body()?.audio_files?.getOrNull(indexOfSura)
                        ?: throw Exception("Sura not found")
                    //voice url
                    runningVoiceUrl= suraVoice.audio_url
                    withContext(Dispatchers.Main) {
                        playFromUrl(runningVoiceUrl)
                        animateOpenSoundPlayer()
                    }
                } else {
                    showError("Failed to fetch reader voice.")
                }
            } catch (e: Exception) {
                showError("Error: check Your internet connection")
            }
        }
    }

    /**
     * Play audio from a given URL.
     */
    private fun playFromUrl(audioUrl: String) {
        binding.playPauseBtn.setImageResource(R.drawable.pause)
        isPlaying = true
        exoPlayer.setMediaItem(MediaItem.fromUri(audioUrl))
        exoPlayer.prepare()
        exoPlayer.play()
    }

    /**
     * Play the current audio.
     */
    private fun playAudio() {
        isPlaying = !isPlaying
        if (isPlaying) {
            binding.playPauseBtn.setImageResource(R.drawable.pause)
            if (exoPlayer.playbackState == ExoPlayer.STATE_ENDED) {
                exoPlayer.seekTo(0) // Restart if playback has ended
            }
            exoPlayer.play()
        } else {
            binding.playPauseBtn.setImageResource(R.drawable.play_icon)
            pauseAudio()
        }
    }

    private fun pauseAudio() {
        exoPlayer.pause()
    }

    private fun stopAudio() {
        exoPlayer.stop()
    }

    private fun showError(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(this@SoundsActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
    fun filterReaders(text:String, list:ArrayList<Reader>):ArrayList<Reader>{
        val filtered= arrayListOf<Reader>()
        for (item in list){
            if (item.name.contains(text, ignoreCase = true)){
                filtered.add(item)
            }
        }
        return filtered
    }
    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isSeeking && exoPlayer.isPlaying) {
                    binding.seekBar.progress = exoPlayer.currentPosition.toInt()
                    binding.currentTime.text = formatTime(exoPlayer.currentPosition)
                }
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(ms: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.deselectTheLastReader()
        handler.removeCallbacksAndMessages(null)
        this.unregisterReceiver(wifiRc)
        exoPlayer.release()
    }
    private fun animateOpenSoundPlayer() {
        val card = binding.soundPlayerCard
        if (!cardIsVisible) {
            cardIsVisible = true
            card.visibility = VISIBLE
            card.animate().scaleY(0f).scaleX(0f).setDuration(0).start()
            card.animate().scaleY(1f).scaleX(1f).setDuration(300).start()
        }
        onShowFirst()
    }


    private fun knowIfFavOrNot ():Boolean {
        val currentId="${indexOfSura+1}${currentReader.id}"
        return isTrackFoundBlocking(currentId)
        }

    private fun onShowFirst(){
        val found=knowIfFavOrNot()
        if (!found){
            isFavorite=false
            binding.favButton.setImageResource(R.drawable.fav_out)
        }
        else{
            binding.favButton.setImageResource(R.drawable.fav_fill)
            isFavorite=true
        }
    }

    private fun addFavTrack(track:FavSuraTrack){
        lifecycleScope.launch(Dispatchers.IO) {
            db.insertTrack(track)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SoundsActivity, "added", Toast.LENGTH_SHORT).show()
            }
        }}
    private fun removeTrack(id:String){
        lifecycleScope.launch(Dispatchers.IO) {
            db.deleteTrack(id)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SoundsActivity, "removed", Toast.LENGTH_SHORT).show()
            }
    }
    }
    private fun isTrackFoundBlocking(id: String): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                val track = db.searchTrack(id)
                return@withContext track != null
            }
        }
    }
    private fun onConnToWiFi(){
        binding.disConnectedView.visibility= GONE
        binding.recyclerView.visibility= VISIBLE
        binding.searchView2.visibility= VISIBLE
        if (isPlaying){
            exoPlayer.play()
            binding.soundPlayerCard.visibility= VISIBLE
        }
    }
    private fun onDisConnToWiFi(){
        exoPlayer.pause()
        binding.disConnectedView.visibility= VISIBLE
        binding.soundPlayerCard.visibility= GONE
        binding.recyclerView.visibility= GONE
        binding.searchView2.visibility= GONE
    }

}
