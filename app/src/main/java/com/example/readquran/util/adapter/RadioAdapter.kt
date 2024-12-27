package com.example.readquran.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.R
import com.example.readquran.databinding.RadioChannelCardBinding
import com.example.readquran.model.Radio
import com.example.readquran.util.adapterListener.RadioListener
import com.squareup.picasso.Picasso

class RadioAdapter(private var data: List<Radio>,private var listener:RadioListener) :
    RecyclerView.Adapter<RadioAdapter.RadioHolder>() {

    // Create ViewHolder class
    class RadioHolder(val binding: RadioChannelCardBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioHolder {
        val binding = RadioChannelCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RadioHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: RadioHolder, position: Int) {
        Picasso.get().load(
            data[position].img
        ).placeholder(
            R.drawable.player_bg
        ).error(
            R.drawable.delete_icon
        )
            .into(holder.binding.radioChannelImage)

        holder.binding.channelName.text = data[position].name
        holder.binding.root.setOnClickListener{
            listener.onClick(data[position],position)
        }
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}