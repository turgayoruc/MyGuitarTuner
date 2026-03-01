package com.example.myguitartuner

data class MyNoteDataClass(val nameEnum: MyEnumString, var highlighting: Boolean, val icon:Int, val frekans: String, val onSelected:(MyNoteDataClass)->Unit)
