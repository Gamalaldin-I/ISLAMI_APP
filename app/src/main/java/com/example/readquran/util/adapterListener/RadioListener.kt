package com.example.readquran.util.adapterListener

import com.example.readquran.model.Radio

interface RadioListener {
    fun onClick(radio: Radio, position: Int)
}