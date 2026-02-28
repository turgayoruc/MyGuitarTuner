package com.example.myguitartuner

data class MyNoteDataClass(var isSelected: Boolean, val icon:Int, val label: String, val onSelected:(MyNoteDataClass)->Unit)
