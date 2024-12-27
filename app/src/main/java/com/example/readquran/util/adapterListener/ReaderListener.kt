package com.example.readquran.util.adapterListener

import com.example.readquran.model.Reader

interface ReaderListener {
    fun onClick(reader: Reader, position: Int)
}