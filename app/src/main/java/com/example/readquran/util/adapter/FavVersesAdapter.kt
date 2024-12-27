package com.example.readquran.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.R
import com.example.readquran.databinding.FavVerseCardBinding
import com.example.readquran.model.FavVerse
import com.example.readquran.util.Animator
import com.example.readquran.util.ArabicTranslator
import com.example.readquran.util.adapterListener.FavouriteListener

class FavVersesAdapter(private var data: List<FavVerse>, private val listener: FavouriteListener) :
    RecyclerView.Adapter<FavVersesAdapter.FavHolder>() {
        private val willBeRemoved: ArrayList<FavVerse> = ArrayList()
    // Create ViewHolder class
    class FavHolder(val binding: FavVerseCardBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
        val binding =
            FavVerseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavHolder(binding)
    }
    fun getWillBeRemoved(): ArrayList<FavVerse> {
        return willBeRemoved
    }
    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val favVerse = data[position]
        holder.binding.ayaNumber.text = ArabicTranslator.toArabicNumerals(data[position].number)
        holder.binding.ayaContent.text = favVerse.text
        holder.binding.suraName.text = favVerse.suraName

        holder.binding.root.setOnClickListener{
            listener.onClick(favVerse, position)
        }
        holder.binding.deleteBtn.setOnClickListener{
            Animator.animateBtn(holder.binding.deleteBtn)
            favVerse.willBeRemoved = !favVerse.willBeRemoved
            onDeleteClickView(holder,position)
            if (favVerse.willBeRemoved){
                willBeRemoved.add(favVerse)
            }
            else{
                willBeRemoved.remove(favVerse)
            }
        }

    }
    private fun onDeleteClickView(holder: FavHolder, position: Int) {
        val favVerse = data[position]
        if (favVerse.willBeRemoved){
            holder.binding.deleteBtn.setImageResource(R.drawable.fav_out)
        }
        else{
            holder.binding.deleteBtn.setImageResource(R.drawable.fav_fill)
        }
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}