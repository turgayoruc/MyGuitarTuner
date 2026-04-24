package com.example.myguitartuner.b_data_katmani.g_repository_dataHarmanlayicisi

import TunerDao
import com.example.myguitartuner.a_domain_katmani.a_model.NoteModel
import com.example.myguitartuner.a_domain_katmani.b_repository.ITunerRepository
import com.example.myguitartuner.b_data_katmani.a_audio.AudioEngine
import com.example.myguitartuner.b_data_katmani.b_local_ya_da_database.a_entity.TunerEntity
import com.example.myguitartuner.b_data_katmani.e_source.TunerDefaults
import com.example.myguitartuner.b_data_katmani.f_mapper_donusturuculer.toEntity
import com.example.myguitartuner.b_data_katmani.f_mapper_donusturuculer.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TunerRepositoryImpl(private val audioEngine: AudioEngine, private val noteDao: TunerDao, private val defaultSource: TunerDefaults): ITunerRepository {
    override fun startEngine(scope: CoroutineScope) {
        audioEngine.startTuningInEngine(scope)
    }

    override fun getLivePitch(): Flow<Double> {
        return audioEngine.rawPitchFlow
    }

    override suspend fun getNoteList(): Flow<List<NoteModel>> {
        val allNotesTypeEntity:Flow<List<TunerEntity>> = noteDao.getAllNotes()
        val allNotesTypeModel: Flow<List<NoteModel>> = allNotesTypeEntity.map { item->
            item.map { it.toModel() }
        }
        return allNotesTypeModel

    }

    override suspend fun updateNoteList(noteList: List<NoteModel>) {
        val entities: List<TunerEntity> = noteList.map { it.toEntity()  }
        noteDao.updateNotes(entities)

    }
}