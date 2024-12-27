package com.example.readquran.util.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.databinding.HistoryItemBinding
import com.example.readquran.model.LastRead
import com.example.readquran.util.adapterListener.HistoryListener


class HistoryAdapter(var data: MutableList<LastRead>, private val listener: HistoryListener) :
    RecyclerView.Adapter<HistoryAdapter.LastReadHolder>() {

    // Create ViewHolder class
    class LastReadHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastReadHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastReadHolder(binding)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<LastRead>) {
        data=newData as MutableList<LastRead>
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: LastReadHolder, position: Int) {
        val item = data[position]
        holder.binding.ayahNumber.text = item.ayahNumber.toString()
        holder.binding.suraName.text = item.suraName
        holder.binding.root.setOnClickListener{
            listener.onClick(item)
        }
        holder.binding.deleteBtn.setOnClickListener{
            listener.onDelete(item,position)
        }
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}