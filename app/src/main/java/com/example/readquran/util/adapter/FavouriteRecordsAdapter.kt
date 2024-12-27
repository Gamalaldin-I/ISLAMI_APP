package com.example.readquran.util.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.R
import com.example.readquran.databinding.ReaderFavCardBinding
import com.example.readquran.model.FavSuraTrack
import com.example.readquran.util.Animator
import com.example.readquran.util.adapterListener.FavouriteRecordListener

class FavouriteRecordsAdapter(private val data: MutableList<FavSuraTrack>, private val listener:FavouriteRecordListener) :
    RecyclerView.Adapter<FavouriteRecordsAdapter.FavHolder>() {
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    private var willRemovedRecords:ArrayList<FavSuraTrack> = ArrayList()
    // Create ViewHolder class
    class FavHolder(val binding:ReaderFavCardBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
        val binding = ReaderFavCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavHolder(binding)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<FavSuraTrack>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val record = data[position]
        onFavUnFavView(holder, position)
        if (record.selected){
            holder.binding.container.setBackgroundResource(R.color.primary)
            holder.binding.suraName.setTextColor(holder.binding.root.context.getColor(R.color.background))
            holder.binding.readerNameTV.setTextColor(holder.binding.root.context.getColor(R.color.background))
        }
        else{
            holder.binding.container.setBackgroundResource(R.color.background)
            holder.binding.suraName.setTextColor(holder.binding.root.context.getColor(R.color.text))
            holder.binding.readerNameTV.setTextColor(holder.binding.root.context.getColor(R.color.text))
        }
        holder.binding.readerNameTV.text = record.readerName
        holder.binding.suraName.text = record.suraName
        holder.binding.root.setOnClickListener {
            selectRecord(position)
            listener.onClick(record, position)
        }
        holder.binding.favourite.setOnClickListener {
            record.willRemove =!record.willRemove
            if (record.willRemove){
                willRemovedRecords.add(record)
            }
            else{
                willRemovedRecords.remove(record)
                Animator.animateBtn(holder.binding.favourite)
            }
            onFavUnFavView(holder, position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun selectRecord(position: Int){
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            data[selectedItemPosition].selected=false}
        data[position].selected=true
        selectedItemPosition=position
        notifyDataSetChanged()
    }
    fun deselectRecord(){
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            data[selectedItemPosition].selected=false
        }
    }
    private fun onFavUnFavView(holder: FavHolder, position: Int){
        if (data[position].willRemove){
            holder.binding.favourite.setImageResource(R.drawable.fav_out)
        }
        else{
            holder.binding.favourite.setImageResource(R.drawable.fav_fill)
        }

    }

    fun getAllThatWillBeRemoved():ArrayList<FavSuraTrack>{
        return willRemovedRecords
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}