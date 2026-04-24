package com.example.myguitartuner.b_data_katmani.f_mapper_donusturuculer

import com.example.myguitartuner.a_domain_katmani.a_model.NoteModel
import com.example.myguitartuner.b_data_katmani.b_local_ya_da_database.a_entity.TunerEntity

// Entity'den Domain Model'e (Veritabanından okurken)
fun TunerEntity.toModel(): NoteModel {
    return NoteModel(
        id = id,
        telAdi = telAdi,
        highlighting = highlighting,
        icon = icon,
        frekansString = frekansString,
        frekansDouble = frekansDouble

                    )
}

// Domain Model'den Entity'ye (Veritabanına kaydederken)
fun NoteModel.toEntity(): TunerEntity {
    return TunerEntity(
        id = id,
        telAdi = telAdi,
        highlighting = highlighting,
        icon = icon,
        frekansString = frekansString,
        frekansDouble = frekansDouble

                      )
}