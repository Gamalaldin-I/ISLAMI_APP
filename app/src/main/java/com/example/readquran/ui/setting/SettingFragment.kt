package com.example.readquran.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.readquran.databinding.FragmentSettingBinding
import com.example.readquran.repo.local.shared.ShardPreference

class SettingFragment : Fragment() {
  private lateinit var binding: FragmentSettingBinding
  private lateinit var pref:ShardPreference
  private var nightMode =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        binding=FragmentSettingBinding.inflate(inflater,container,false)
        pref= ShardPreference(this.requireContext())
        val fontSize=pref.getFontSize()
        nightMode=pref.getNightMode()
        binding.progressbar.progress=fontSize.toInt()
        binding.switchMood.isChecked=nightMode
        getTheme(nightMode)
        changeTextSize(fontSize)
        listenChangeINSeekbar()
        setUpeNightMode()

        return binding.root
    }

    private fun listenChangeINSeekbar(){
        binding.progressbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    changeTextSize(progress.toFloat())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }
    private fun changeTextSize(newSize: Float){
        binding.textToChange.textSize=newSize
        pref.setFontSize(newSize)
    }
    private fun setUpeNightMode(){
        binding.switchMood.setOnClickListener{
            nightMode=binding.switchMood.isChecked
            getTheme(nightMode)
            pref.setNightMode(nightMode,true)
        }
    }
    private fun getTheme(nightMode: Boolean){
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }




}