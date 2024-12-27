package com.example.readquran.ui.radio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.readquran.R
import com.example.readquran.databinding.ActivityRadioChannelBinding
import com.squareup.picasso.Picasso

class RadioChannelActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRadioChannelBinding
    private  var exoPlayer: ExoPlayer? = null
    private var isPlaying  = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRadioChannelBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        val radioUrl=intent.getStringExtra("radioUrl")!!
        val radioName=intent.getStringExtra("radioName")
        val radioImage=intent.getStringExtra("radioImage")
        setupExoPlayer()
        setupControls()
        playFromUrl(radioUrl)
        binding.nameOfRadioChannel.text=radioName
        Picasso.get().load(radioImage).placeholder(R.drawable.player_bg).into(binding.imageToShow)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener {
            finish()
        }

        // SeekBar change listener

    }


    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
    }
    //setup controls
    private fun setupControls() {
        binding.playPauseBtn.setOnClickListener {playAudio()}
    }



    // audio functions
    //play audio from url that calls the playFromUrl function if the audio clicked to open
    private fun playFromUrl(audioUrl: String) {
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



    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
        exoPlayer?.release()
        exoPlayer?.clearMediaItems()
        exoPlayer = null

    }
}