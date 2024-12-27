package com.example.readquran.util.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.databinding.AyahItemSearchBinding
import com.example.readquran.model.Ayah
import com.example.readquran.quranData.QuranInfo
import com.example.readquran.util.ArabicTranslator
import com.example.readquran.util.adapterListener.AyatSearchListener


class SearchAyatAdapter(private var data: List<Ayah>, private val listener: AyatSearchListener) :
    RecyclerView.Adapter<SearchAyatAdapter.JuzeHolder>() {

    // Create ViewHolder class
    class JuzeHolder(val binding:AyahItemSearchBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuzeHolder {
        val binding = AyahItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JuzeHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: JuzeHolder, position: Int) {
        val data = data[position]
        holder.binding.suraName.text=QuranInfo.map[data.surahId]
        holder.binding.ayaContent.text=data.text
        holder.binding.ayaNumber.text= ArabicTranslator.toArabicNumerals(data.numberInSurah)
        holder.binding.root.setOnClickListener{
            listener.onClick(data)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: List<Ayah>) {
        data=filteredList
        notifyDataSetChanged()
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}