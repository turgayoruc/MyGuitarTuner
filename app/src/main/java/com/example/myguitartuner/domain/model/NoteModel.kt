package com.example.myguitartuner.domain.model

import com.example.myguitartuner.domain.model.StringName

data class NoteModel(var nameEnum: StringName, var highlighting: Boolean, val icon:Int, val frekans: String)