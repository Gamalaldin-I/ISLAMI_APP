package com.example.readquran.util.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.R
import com.example.readquran.databinding.ReaderNameCardBinding
import com.example.readquran.model.Reader
import com.example.readquran.util.adapterListener.ReaderListener

class ReadersAdapter(private val data: ArrayList<Reader>, private val listener: ReaderListener) :
    RecyclerView.Adapter<ReadersAdapter.ReaderViewHolder>() {
        private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    // Create ViewHolder class
    class ReaderViewHolder(val binding: ReaderNameCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReaderViewHolder {
        val binding =
            ReaderNameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReaderViewHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ReaderViewHolder, position: Int) {
        val reader = data[position]

        if (reader.selected){
            holder.binding.container.setBackgroundResource(R.color.gradient3)
            holder.binding.readerNameTV.setTextColor(holder.binding.root.context.getColor(R.color.background))
        }
        else{
        holder.binding.container.setBackgroundResource(R.drawable.selected_reader_bg)
            holder.binding.readerNameTV.setTextColor(holder.binding.root.context.getColor(R.color.text))
        }
        holder.binding.readerNameTV.text = reader.name
        holder.binding.root.setOnClickListener {
            selectReader(position)
            listener.onClick(reader, position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun selectReader(position: Int){
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
        data[selectedItemPosition].selected=false}
        data[position].selected=true
        selectedItemPosition=position
        notifyDataSetChanged()
    }
    fun deselectTheLastReader(){
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            data[selectedItemPosition].selected=false
        }
    }


    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}