package com.example.myguitartuner.a_domain_katmani.a_model



data class NoteModel(
    val id: Int = 0,//SQL icin buad sart.
    val telAdi: String,
    var highlighting: Boolean,
    val icon:Int,
    val frekansString: String,
    val frekansDouble: Double)