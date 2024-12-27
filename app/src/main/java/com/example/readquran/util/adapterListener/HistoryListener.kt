package com.example.readquran.util.adapterListener

import com.example.readquran.model.LastRead


interface HistoryListener {
    fun onClick(last: LastRead)
    fun onDelete(last:LastRead,position:Int)
}
