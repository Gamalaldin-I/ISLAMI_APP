package com.example.readquran.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readquran.databinding.ZekrCategoryItemBinding
import com.example.readquran.model.ZekrCategory
import com.example.readquran.util.adapterListener.ZekrCategoryInterface

class ZekrCategoriesAdapter(private val data: ArrayList<ZekrCategory>,private val listener: ZekrCategoryInterface) :
    RecyclerView.Adapter<ZekrCategoriesAdapter.CategoryName>() {

    // Create ViewHolder class
    class CategoryName(val binding: ZekrCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryName {
        val binding = ZekrCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryName(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: CategoryName, position: Int) {
        val category = data[position]
        holder.binding.categoryName.text = category.name
        holder.binding.root.setOnClickListener {
            listener.onClick(category,position)
        }

    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return data.size
    }
}