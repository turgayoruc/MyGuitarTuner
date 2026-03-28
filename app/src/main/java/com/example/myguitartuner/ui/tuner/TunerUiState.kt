package com.example.myguitartuner.ui.tuner

import androidx.compose.ui.graphics.Color
import com.example.myguitartuner.domain.model.NoteModel
import com.example.myguitartuner.data.repository.TunerDefaults

data class TunerUiState(
        //Buradaki tum degiskenlerin turlerini belirlemek zorundasin.
    val pitch: Int = 0,
    val target: Double=440.0,
    val yuzde: Int = 0,
    val renk: Color = Color.Companion.Green,
    val kremRengi: Color = Color(0xFFFFFDD0),

    val string1: List<NoteModel> = TunerDefaults.initialString1,
    val string2: List<NoteModel> = TunerDefaults.initialString2,
    val string3: List<NoteModel> = TunerDefaults.initialString3,
    val string4: List<NoteModel> = TunerDefaults.initialString4,
    val string5: List<NoteModel> = TunerDefaults.initialString5,
    val string6: List<NoteModel> = TunerDefaults.initialString6,
    val stringDefault: List<NoteModel> = TunerDefaults.initialStringDefault,

    val selectedBirinci: NoteModel = TunerDefaults.initialString1[4],
    val selectedIkinci: NoteModel = TunerDefaults.initialString2[4],
    val selectedUcuncu: NoteModel = TunerDefaults.initialString3[4],
    val selectedDorduncu: NoteModel = TunerDefaults.initialString4[4],
    val selectedBesinci: NoteModel = TunerDefaults.initialString5[4],
    val selectedAltinci: NoteModel = TunerDefaults.initialString6[4],

    val activeString: List<NoteModel> = TunerDefaults.initialString1, //Ilk olusturulurken null yerine string1.value yaptim sadece.

    val screenWidthDp: Float = 0f,

    val popupAcikMi: Boolean=false


                       )