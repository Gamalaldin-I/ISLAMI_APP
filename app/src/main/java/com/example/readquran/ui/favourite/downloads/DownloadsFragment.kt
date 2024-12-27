package com.example.readquran.ui.favourite.downloads
import Dialogs
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.readquran.R
import com.example.readquran.databinding.FragmentDownloadsBinding
import com.example.readquran.model.DownloadedFile
import com.example.readquran.util.Animator
import com.example.readquran.util.Downloader
import com.example.readquran.util.adapter.DownloadsAdapter
import com.example.readquran.util.adapterListener.DownloadFilesListener


class DownloadsFragment : Fragment(), DownloadFilesListener {
private lateinit var binding: FragmentDownloadsBinding
    private  var exoPlayer: ExoPlayer? = null
    private var isPlaying = false
    private var isSeeking = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var filesList: ArrayList<DownloadedFile>
    private lateinit var viewModel: DownLoadFragmentViewModel
    private lateinit var adapter: DownloadsAdapter
    private var selectedAll = false
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[DownLoadFragmentViewModel::class.java]
        // Get the list of downloaded files
        filesList = getFilesFromDownloadsFolder()

        //setup recycler view
        setupRecyclerView()
        //setup exo player
        setupExoPlayer()
        //setup controls
        setupControls()
        requireActivity().onBackPressedDispatcher.addCallback(this.requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(adapter.isSelectCase()){
                    adapter.onBackPressed()
                }
                else{
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        // Exit the app
                        backToast.cancel() // Dismiss the previous toast
                        finishAffinity(requireActivity())   // Close the app and all its activities
                    } else {
                        // Notify the user
                        backPressedTime = currentTime
                        backToast = Toast.makeText(requireContext(), "اضغط مرة أخرى للخروج", Toast.LENGTH_SHORT)
                        backToast.show()
                    }
                }
            }
        })


        // Listeners
        // Listen for playback state changes
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

        // SeekBar change listener  // if the user touch it to change the time
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
                // to go to the new part that the user go to
                isSeeking = false
                exoPlayer?.seekTo(seekBar?.progress?.toLong() ?: 0)
            }
        })
        // to stop the audio when click on the close button
        binding.closeBtn.setOnClickListener {
            Animator.animateClosingCardOfExoPlayer(binding.soundPlayerCard)
            stopAudio()
        }
        binding.selectAll.setOnClickListener {
            selectedAll = !selectedAll
            if (selectedAll){
                adapter.deSelectAll()
                binding.selectAll.setImageResource(R.drawable.multi_select_icon)
            }else{
                adapter.selectAll()
                binding.selectAll.setImageResource(R.drawable.multi_selected_icon)
            }
        }
        binding.deleteBtn.setOnClickListener {
            onDelete()
        }

        return view
    }

   // on click the card of reader that will be played
    override fun onPlay(file: DownloadedFile, position: Int) {
        playTheAudio(file)
    }

     private fun onDelete() {
        Dialogs.showAlertDialog(requireContext()," سوف يتم الحذف نهائيا ..",
            "حذف",
            "نعم",
            "لا",
            {val deletedFiles = adapter.deletedData()
                if (deletedFiles.isEmpty()){
                    Toast.makeText(requireContext(), "لا يوجد ملفات لتشغيلها", Toast.LENGTH_SHORT).show()
                    return@showAlertDialog
                }
                Toast.makeText(requireContext(), "${deletedFiles.size}", Toast.LENGTH_SHORT).show()
                for (file in deletedFiles){
                    Downloader.deleteDownloadedTrackMP3(requireContext(),file.fileName)
                }
                filesList=getFilesFromDownloadsFolder()
                adapter.updateData(filesList)
                Toast.makeText(requireContext(), "تم الحذف", Toast.LENGTH_SHORT).show()},
            {})
    }

    override fun onLongClick(file: DownloadedFile, position: Int) {
        Animator.showToolbar(binding.controlPannar)
    }

    override fun onDeSelect() {
        Animator.hideToolbar(binding.controlPannar)
        //binding.controlPannar.visibility = View.GONE
    }

    override fun isAllSelected(isAllSelected: Boolean) {
          if (isAllSelected){
            binding.selectAll.setImageResource(R.drawable.multi_selected_icon)}
          else
          {binding.selectAll.setImageResource(R.drawable.multi_select_icon)}
             selectedAll = isAllSelected

    }




    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
    }
    private fun setupControls() {
        binding.playPauseBtn.setOnClickListener {controlClickingOnPlayBtn()}
    }
    private fun setupRecyclerView(){
        adapter= DownloadsAdapter(filesList,this)
        binding.recyclerView.adapter= adapter
    }


    private fun controlClickingOnPlayBtn() {
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
    //controllers
    //play audio
    private fun playTheAudio(file: DownloadedFile) {
        binding.playPauseBtn.setImageResource(R.drawable.pause)
        isPlaying = true
        binding.soundPlayerCard.visibility =VISIBLE
        Animator.animateAppearanceOfCardOfExoPlayer(binding.soundPlayerCard)
        val fileName= file.fileName.removeSuffix(".mp3")
        binding.readerNameTV.text= fileName
        val filePath= file.filePath
        exoPlayer?.setMediaItem(MediaItem.fromUri(filePath))
        exoPlayer?.prepare()
        exoPlayer?.play()
    }
   //pause audio
    private fun pauseAudio() {
        exoPlayer?.pause()
    }
    //stop audio
    private fun stopAudio() {
        exoPlayer?.stop()
    }
    //update seek bar with the remaining time
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



    override fun onDestroyView() {
        super.onDestroyView()
        stopAudio()
        exoPlayer?.release()
        exoPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
    private fun getFilesFromDownloadsFolder(): ArrayList<DownloadedFile>{
        val files = ArrayList<DownloadedFile>()
        val downloadsFolder = Downloader.getDownloadedTrackMP3(requireContext())
        if (downloadsFolder.isNotEmpty()){
            for (file in downloadsFolder){
                files.add(DownloadedFile(file.name,file.path))
            }
        }
        return files
    }




}