package com.example.myguitartuner.a_domain_katmani.b_repository


import com.example.myguitartuner.a_domain_katmani.a_model.NoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

//Burasi tum veri giric cikislarinin ana yonetim merkezi
interface ITunerRepository {
    fun startEngine(scope: CoroutineScope)
    fun getLivePitch(): Flow<Double> // Mikrofondan gelen canlı veri

    suspend fun getNoteList(): Flow<List<NoteModel>>
    suspend fun updateNoteList(noteList: List<NoteModel>)

}