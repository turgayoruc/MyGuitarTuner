package com.example.myguitartuner.d_ui_katmani.d_intents

import androidx.compose.ui.graphics.Color
import com.example.myguitartuner.a_domain_katmani.a_model.NoteModel

sealed class TunerIntent {

    // Uygulama ilk açıldığında veritabanından ayarları yüklemek için. DataState, Room,internetten veri cekme islemleri burada.
    object LoadSavedSettings : TunerIntent()

    object StartEngine : TunerIntent() // Motoru başlatma niyeti

    data class UpdateStateOfPitchYuzdeRenk(val pitch: Double) : TunerIntent()

    data class UpdateYuzde(val yuzde: Int) : TunerIntent()

    data class UpdateRenk(val renk: Color) : TunerIntent()

    data class UpdateTarget(val target: Double) : TunerIntent()

    data class UpdateString1(val string1: List<NoteModel>) : TunerIntent()
    data class UpdateString2(val string2: List<NoteModel>) : TunerIntent()
    data class UpdateString3(val string3: List<NoteModel>) : TunerIntent()
    data class UpdateString4(val string4: List<NoteModel>) : TunerIntent()
    data class UpdateString5(val string5: List<NoteModel>) : TunerIntent()
    data class UpdateString6(val string6: List<NoteModel>) : TunerIntent()

    data class UpdateSelectedBirinci(val selectedBirinci: NoteModel) : TunerIntent()
    data class UpdateSelectedIkinci(val selectedIkinci: NoteModel) : TunerIntent()
    data class UpdateSelectedUcuncu(val selectedUcuncu: NoteModel) : TunerIntent()
    data class UpdateSelectedDorduncu(val selectedDorduncu: NoteModel) : TunerIntent()
    data class UpdateSelectedBesinci(val selectedBesinci: NoteModel) : TunerIntent()
    data class UpdateSelectedAltinci(val selectedAltinci: NoteModel) : TunerIntent()

    data class UpdateSelectedAllString(val selectedItem: NoteModel) : TunerIntent()
    data class UpdateHighlighting(val selectedItem: NoteModel) : TunerIntent()

    data class UpdateActiveString(val activeString: List<NoteModel>) : TunerIntent()



    data class UpdateScreenWidth(val widthDp: Float) : TunerIntent()
    data class UpdatePopupAcikMi(val popupAcikMi: Boolean) : TunerIntent()

    //Room icin
    data class UpdateHighlightingSQLite(val note: NoteModel) : TunerIntent()
}