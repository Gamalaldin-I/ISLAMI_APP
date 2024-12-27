package com.example.readquran.util.adapterListener

import com.example.readquran.model.ZekrCategory

interface ZekrCategoryInterface {

    fun onClick(
        category: ZekrCategory,
        position: Int,
    )
}