package com.example.myguitartuner

data class MyNoteDataClass(var nameEnum: MyEnumString, var highlighting: Boolean, val icon:Int,  val frekans: String, @Transient var onSelected:(MyNoteDataClass)->Unit)
