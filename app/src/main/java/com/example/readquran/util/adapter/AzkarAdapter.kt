package com.example.readquran.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.databinding.ZekrCardLoBinding
import com.example.readquran.jsonParser.ZekrParser

class AzkarAdapter(private val data: List<ZekrParser>) :
    RecyclerView.Adapter<AzkarAdapter.ZekrHolder>() {

    // Create ViewHolder class
    class ZekrHolder(val binding: ZekrCardLoBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZekrHolder {
        val binding = ZekrCardLoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZekrHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ZekrHolder, position: Int) {
        holder.binding.zekrText.text = data[position].content
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}