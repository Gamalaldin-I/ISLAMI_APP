package com.example.readquran.ui.radio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.readquran.databinding.FragmentRadioBinding
import com.example.readquran.model.Radio
import com.example.readquran.repo.remote.RetrofitBuilder
import com.example.readquran.util.adapter.RadioAdapter
import com.example.readquran.util.adapterListener.RadioListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RadioFragment : Fragment(),RadioListener {
private lateinit var binding: FragmentRadioBinding
private lateinit var adapter:RadioAdapter
private var radioChannels:List<Radio> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentRadioBinding.inflate(inflater,container,false)
        adapter=RadioAdapter(radioChannels,this)
        binding.recyclerView.adapter=adapter
        fetchReaderVoice()



        return binding.root
    }

    private fun fetchReaderVoice() {
        lifecycleScope.launch {
            try {
                val radioResponse = withContext(Dispatchers.IO) {
                    RetrofitBuilder.radioApi.getRadioStations()
                }
                if(radioResponse.isSuccessful){
                    withContext(Dispatchers.Main){
                        adapter=RadioAdapter(radioResponse.body()?.radios!!,this@RadioFragment)
                        binding.recyclerView.adapter=adapter
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                // Handle exceptions, e.g., network issues, API errors
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                // Show an error message to the user
            }
        }
    }

    override fun onClick(radio: Radio, position: Int) {
        val intent=Intent(requireContext(), RadioChannelActivity::class.java)
        intent.putExtra("radioUrl",radio.url)
        intent.putExtra("radioName",radio.name)
        intent.putExtra("radioImage",radio.img)
        startActivity(intent)
    }



}