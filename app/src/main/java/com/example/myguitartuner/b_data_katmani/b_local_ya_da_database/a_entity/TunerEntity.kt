package com.example.myguitartuner.b_data_katmani.b_local_ya_da_database.a_entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class TunerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,//SQL icin buad sart.
    val telAdi: String,
    var highlighting: Boolean,
    val icon:Int,
    val frekansString: String,
    val frekansDouble: Double)