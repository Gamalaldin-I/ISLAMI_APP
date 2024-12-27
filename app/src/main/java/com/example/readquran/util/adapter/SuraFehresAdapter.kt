package com.example.readquran.util.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.databinding.SuraCardBinding
import com.example.readquran.model.Surah
import com.example.readquran.util.ArabicTranslator
import com.example.readquran.util.adapterListener.FehresAdapterListener


class SuraFehresAdapter(private var data: List<Surah>, private val listener: FehresAdapterListener) :
    RecyclerView.Adapter<SuraFehresAdapter.SurahCardHolder>() {

    // Create ViewHolder class
    class SurahCardHolder(val binding: SuraCardBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahCardHolder {
        val binding = SuraCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurahCardHolder(binding)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: List<Surah>) {
        data=filteredList
        notifyDataSetChanged()
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: SurahCardHolder, position: Int) {
        val card=holder.binding
        card.numberOfSurah.text = ArabicTranslator.toArabicNumerals( data[position].surahNumber)
        card.arabicName.text = data[position].arabicName
        card.englishName.text = data[position].englishName
        card.revelationType.text = ArabicTranslator.meccanOrMedinan(data[position].revelationType)
        card.numberOfVerses.text= ArabicTranslator.toArabicNumerals(data[position].numberOfVerses)
        card.root.setOnClickListener {

            listener.surahOnClicked(data[position],position)
            card.root.isEnabled=false
            // Delay the enable state for 1,5 second
            card.root.postDelayed({
                card.root.isEnabled=true
            },1500)
        }
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}