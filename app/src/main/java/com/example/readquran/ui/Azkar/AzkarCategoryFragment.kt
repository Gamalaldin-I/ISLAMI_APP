package com.example.readquran.ui.Azkar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.readquran.databinding.FragmentAzkarCategoryBinding

class AzkarCategoryFragment : Fragment() {
private lateinit var binding: FragmentAzkarCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAzkarCategoryBinding.inflate(inflater, container, false)

        binding.tasabeehCard.setOnClickListener {
            openReadingZekrActivity(8)
        }
        binding.afterSalatCard.setOnClickListener {
            openReadingZekrActivity(1)
        }
        binding.wakeupCard.setOnClickListener {
            openReadingZekrActivity(2)
        }
        binding.seepingCard.setOnClickListener {
            openReadingZekrActivity(7)
        }
        binding.napiDoaCard.setOnClickListener {
            openReadingZekrActivity(5)
        }
        binding.quranDoaCard.setOnClickListener {
            openReadingZekrActivity(6)
        }
        binding.eveningCard.setOnClickListener {
            openReadingZekrActivity(3)
        }
        binding.morningCard.setOnClickListener {
            openReadingZekrActivity(4)
        }
        return binding.root
    }

    private fun openReadingZekrActivity(zekrId: Int) {
        val intent = Intent(requireContext(), ReadingZekrActivity::class.java)
        intent.putExtra("azkarId", zekrId)
        startActivity(intent)
    }

}