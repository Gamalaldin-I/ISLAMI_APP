package com.example.readquran.ui.favourite.favouriteTracks

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.readquran.R
import com.example.readquran.databinding.FragmentFavReadersBinding
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.util.Animator
import com.example.readquran.util.Downloader
import com.example.readquran.util.WifiBroadcastReceiver
import com.example.readquran.util.adapter.FavouriteRecordsAdapter
import com.example.readquran.util.adapterListener.FavouriteRecordListener


@Suppress("DEPRECATION")
class FavTracksFragment : Fragment(),FavouriteRecordListener {
  private lateinit var binding: FragmentFavReadersBinding
  private  var exoPlayer: ExoPlayer? = null
    private var isPlaying = false
    private lateinit var adapter: FavouriteRecordsAdapter
    private lateinit var favTracks: List<FavSuraTrack>
    private lateinit var db: LocalRepoImp
    private lateinit var viewModel: FavTracksViewModel
    private lateinit var wifiRc: WifiBroadcastReceiver
    private var currentUrl: String? = null
    private var currentReaderName: String? = null
    private var currentSuraName: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var isSeeking = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        db= LocalRepoImp(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentFavReadersBinding.inflate(inflater, container, false)
        //initialize view model
        viewModel = ViewModelProvider(this)[FavTracksViewModel::class.java]
        //observe data
        //setup adapter , exoplayer , controls
        viewModel.favTracks.observe(this.viewLifecycleOwner){
            favTracks=it
            setupAdapter(favTracks)
        }

        binding.searchView2.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                val filteredList = filterBySura(s.toString())
                setupAdapter(filteredList)
                return true
            }

        })
        viewModel.getFavTracks(db)
        setupExoPlayer()
        setupControls()
        wifiRc=WifiBroadcastReceiver({ onConnToWiFi() }, {onDisConnToWiFi()})
        //to register the receiver and keep look at wifi status
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
         requireContext().registerReceiver(wifiRc,filter)


        //listeners
        //close button
        binding.closeBtn.setOnClickListener {
            Animator.animateClosingCardOfExoPlayer(binding.soundPlayerCard)
            stopAudio()
        }
        //exo player listener
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_READY) {
                    val durationMs = exoPlayer?.duration
                    binding.seekBar.max = durationMs!!.toInt()
                    binding.duration.text = viewModel.formatTime(durationMs)
                    updateSeekBar()
                }
            }
        })
        // SeekBar change listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.currentTime.text = viewModel.formatTime(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                exoPlayer?.seekTo(seekBar?.progress?.toLong() ?: 0)
            }
        })

        binding.downLoadBtn.setOnClickListener {
            Animator.animateBtn(binding.downLoadBtn)
            Downloader.downloadTrackMP3FromUrlInTheInnerStorageOfApp(
                currentUrl!!,
                requireContext(),
                currentSuraName!!,
                currentReaderName!!)
        }
        //view
        return binding.root
    }
    // setup adapter with the current favourite tracks
    private fun setupAdapter(favTracks: List<FavSuraTrack>){
        adapter= FavouriteRecordsAdapter(favTracks.toMutableList(),this)
        binding.recyclerView.adapter=adapter
    }
    //setup exoplayer
    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
    }
    //setup controls
    private fun setupControls() {
        binding.playPauseBtn.setOnClickListener {playAudio()}
    }



    // audio functions
    //play audio from url that calls the playFromUrl function if the audio clicked to open
    private fun playFromUrl(audioUrl: String) {
        currentUrl = audioUrl
        binding.playPauseBtn.setImageResource(R.drawable.pause)
        isPlaying = true
        exoPlayer?.setMediaItem(MediaItem.fromUri(audioUrl))
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    private fun playAudio() {
        isPlaying = !isPlaying
        if (isPlaying) {
            binding.playPauseBtn.setImageResource(R.drawable.pause)
            if (exoPlayer?.playbackState == ExoPlayer.STATE_ENDED) {
                exoPlayer?.seekTo(0) // Restart if playback has ended
            }
            exoPlayer?.play()
        } else {
            binding.playPauseBtn.setImageResource(R.drawable.play_icon)
            pauseAudio()
        }
    }
    private fun pauseAudio() {
        exoPlayer?.pause()
    }
    private fun stopAudio() {
        exoPlayer?.stop()
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isSeeking && exoPlayer!!.isPlaying) {
                    binding.seekBar.progress = exoPlayer?.currentPosition!!.toInt()
                    binding.currentTime.text = viewModel.formatTime(exoPlayer!!.currentPosition)
                }
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }


    override fun onClick(fav: FavSuraTrack, pos: Int) {
        binding.readerNameTV.text=fav.readerName
        currentUrl=fav.url
        currentReaderName=fav.readerName
        currentSuraName=fav.suraName
        // Stop any currently playing audio
        stopAudio()

        // Fetch the audio URL for the selected reader
        playFromUrl(fav.url)
        binding.soundPlayerCard.visibility= VISIBLE
        Animator.animateAppearanceOfCardOfExoPlayer(binding.soundPlayerCard)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.deselectRecord()
        handler.removeCallbacksAndMessages(null)
        exoPlayer!!.release()
        exoPlayer = null
        requireContext().unregisterReceiver(wifiRc)
        viewModel.removeRecords(adapter.getAllThatWillBeRemoved(),db)
        }

   private fun filterBySura(sura: String):List<FavSuraTrack> {
       val filteredList = ArrayList<FavSuraTrack>()
        for (fav in favTracks) {
            if (fav.suraName.contains(sura)) {
                filteredList.add(fav)
            }
        }
       return filteredList
    }


    private fun onConnToWiFi(){
        binding.disConnectedView.visibility= GONE
        binding.recyclerView.visibility= VISIBLE
        binding.searchView2.visibility= VISIBLE
        if (isPlaying){
            exoPlayer!!.play()
            binding.soundPlayerCard.visibility= VISIBLE
        }
    }
    private fun onDisConnToWiFi(){
        exoPlayer!!.pause()
        binding.disConnectedView.visibility= VISIBLE
        binding.soundPlayerCard.visibility= GONE
        binding.recyclerView.visibility= GONE
        binding.searchView2.visibility= GONE
    }
}