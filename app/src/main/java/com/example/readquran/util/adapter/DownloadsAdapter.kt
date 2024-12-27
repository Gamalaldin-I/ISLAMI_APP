package com.example.readquran.util.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.R
import com.example.readquran.databinding.AudioFileBinding
import com.example.readquran.model.DownloadedFile
import com.example.readquran.util.adapterListener.DownloadFilesListener

class DownloadsAdapter(private val data: ArrayList<DownloadedFile>,private val listener: DownloadFilesListener) :
    RecyclerView.Adapter<DownloadsAdapter.FileHolder>() {
        private var selectCase = false
         var selected = ArrayList<DownloadedFile>()
          var playingPos = -1

    // Create ViewHolder class
    class FileHolder(val binding: AudioFileBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val binding = AudioFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileHolder(binding)
    }
    fun isSelectCase():Boolean{
        return selectCase
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onBackPressed(){
        deSelectAll()
        selectCase=false
        listener.onDeSelect()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<DownloadedFile>) {
        listener.onDeSelect()
        selectCase=false
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
    fun deletedData(): ArrayList<DownloadedFile> {
        val deletedData = if (selected.isEmpty()){
            arrayListOf()
        }else{
            selected

        }
        selected= ArrayList()
        return deletedData
    }

    // Bind data to the ViewHolder
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        onCheckBoxAppear(holder,position)
        isPlaying(holder,position)
        val file = data[position]
        val name = file.fileName.removeSuffix(".mp3")
        val suraName=name.split(" ")[0]
        val reader= name.removeRange(0,suraName.length+1)
        holder.binding.suraName.text = suraName
        holder.binding.readerNameTV.text = reader
        holder.binding.root.setOnClickListener {
            if(!selectCase){
                listener.onPlay(file, position)
                playingPos = position
                notifyDataSetChanged()
            }
            else{
                onSelectCase(holder,file)
            }

        }
        holder.binding.checkbox.setOnClickListener {
            onSelectCase(holder,file)
        }

        holder.binding.root.setOnLongClickListener{
            if(!selectCase){
                selectCase = true
                listener.onLongClick(file, position)
                notifyDataSetChanged()
                file.selected = true
                selected.add(file)
            }
            true
        }
    }

    private fun onCheckBoxAppear(holder: FileHolder,position: Int){
        if(selectCase){
            holder.binding.checkbox.visibility = VISIBLE
            holder.binding.checkbox.isChecked = data[position].selected
        }
        else{
            holder.binding.checkbox.visibility = GONE
        }
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun onSelectCase(holder: FileHolder, file: DownloadedFile){
        file.selected = !file.selected
        holder.binding.checkbox.isChecked = file.selected
        if(file.selected){
            selected.add(file)
        }
        else{
            selected.remove(file)
        }
        //check if all items are selected
        listener.isAllSelected(allSelected())
        //check if no items are selected to hide the checkboxes and
        // notify the adapter that the selectCase is false
        onThatTheLastItemDeSelection()

    }
    @SuppressLint("NotifyDataSetChanged")
    fun deSelectAll(){
        for (i in 0 until selected.size){
            selected[i].selected = false
        }
        selected.clear()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun selectAll(){
        selected.clear()
        for (i in 0 until data.size){
            data[i].selected = true
            selected.add(data[i])
        }
        // the check box will be un checked and will not be hidden until changing the selectCase to false
        notifyDataSetChanged()
    }
    private fun isPlaying(holder: FileHolder,position: Int){
        if(position==playingPos){
            holder.binding.container.setBackgroundResource(R.color.gradient3)
            holder.binding.readerNameTV.setTextColor(holder.binding.root.context.getColor(R.color.background))
            holder.binding.suraName.setTextColor(holder.binding.root.context.getColor(R.color.background))
        }
        else{
            holder.binding.container.setBackgroundResource(R.color.background)
            holder.binding.readerNameTV.setTextColor(holder.binding.root.context.getColor(R.color.primary))
            holder.binding.suraName.setTextColor(holder.binding.root.context.getColor(R.color.primary))
        }
    }

    private fun allSelected():Boolean{
        return selected.size==data.size
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun onThatTheLastItemDeSelection(){
        if(selected.isEmpty()){
            selectCase = false
            listener.onDeSelect()
            notifyDataSetChanged()
        }
    }
}