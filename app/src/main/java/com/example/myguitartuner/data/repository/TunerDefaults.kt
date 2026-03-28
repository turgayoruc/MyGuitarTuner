package com.example.myguitartuner.data.repository

import com.example.myguitartuner.R
import com.example.myguitartuner.domain.model.NoteModel
import com.example.myguitartuner.domain.model.StringName

object TunerDefaults {

    val initialString1: List<NoteModel> = listOf(
        NoteModel(StringName.STRING1, false, R.drawable.c_, "C4-261.63 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.d_, "D4-293.66 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.ddiyez_, "D♯4 / E♭4-311.13 Hz"),
        NoteModel(StringName.STRING1, true, R.drawable.e_, "E4-329.63 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.f_, "F4-349.23 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.fdiyez_, "F♯4 / G♭4-369.99 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.g_, "G4-392.00 Hz"),
        NoteModel(StringName.STRING1, false, R.drawable.gdiyez_, "G#4-415.30 Hz")
                                                )
    val initialString2: List<NoteModel> = listOf(
        NoteModel(StringName.STRING2, false, R.drawable.g_, "G3-196.00 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.a_, "A3-220.00 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz"),
        NoteModel(StringName.STRING2, true, R.drawable.b_, "B3-246.94 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.c_, "C4-261.63 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.d_, "D4-293.66 Hz"),
        NoteModel(StringName.STRING2, false, R.drawable.ddiyez_, "D#4-311.13 Hz")
                                                )
    val initialString3: List<NoteModel> = listOf(
        NoteModel(StringName.STRING3, false, R.drawable.ddiyez_, "D♯3 / E♭3-155.56 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.e_, "E3-164.81 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.f_, "F3-174.61 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz"),
        NoteModel(StringName.STRING3, true, R.drawable.g_, "G3-196.00 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.a_, "A3-220.00 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz"),
        NoteModel(StringName.STRING3, false, R.drawable.b_, "B3-246.94 Hz")
                                                )
    val initialString4: List<NoteModel> = listOf(
        NoteModel(StringName.STRING4, false, R.drawable.adiyez_, "B♭2-116.54 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.b_, "B2-123.47 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.c_, "C3-130.81 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz"),
        NoteModel(StringName.STRING4, true, R.drawable.d_, "D3-146.83 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.ddiyez_, "D♯3 / E♭3-155.56 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.e_, "E3-164.81 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.f_, "F3-174.61 Hz"),
        NoteModel(StringName.STRING4, false, R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz")
                                                )
    val initialString5: List<NoteModel> = listOf(
        NoteModel(StringName.STRING5, false, R.drawable.f_, "F2-87.31 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.fdiyez_, "F♯2 / G♭2-92.50 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.g_, "G2-98.00 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.gdiyez_, "G♯2 / A♭2-103.83 Hz"),
        NoteModel(StringName.STRING5, true, R.drawable.a_, "A2-110.00 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.adiyez_, "A#2-116.54 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.b_, "B2-123.47 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.c_, "C3-130.81 Hz"),
        NoteModel(StringName.STRING5, false, R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz")
                                                )
    val initialString6: List<NoteModel> = listOf(
        NoteModel(StringName.STRING6, false, R.drawable.c_, "C2-65.41 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.cdiyez_, "C♯2 / D♭2-69.30 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.d_, "D2-73.42 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.ddiyez_, "D♯2 / E♭2-77.78 Hz"),
        NoteModel(StringName.STRING6, true, R.drawable.e_, "E2-82.41 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.f_, "F2-92.50 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.fdiyez_, "F♯2 / G♭2-98.00 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.g_, "G2-103.83 Hz"),
        NoteModel(StringName.STRING6, false, R.drawable.gdiyez_, "G#2-110.00 Hz")
                                                )
    val initialStringDefault: List<NoteModel> = listOf(
        NoteModel(StringName.STRINGDEFAULT, true, R.drawable.e_, "E4-329.63 Hz"),
        NoteModel(StringName.STRINGDEFAULT, true, R.drawable.b_, "B3-246.94 Hz"),
        NoteModel(StringName.STRINGDEFAULT, true, R.drawable.g_, "G3-196.00 Hz"),
        NoteModel(StringName.STRINGDEFAULT, true, R.drawable.d_, "D3-146.83 Hz"),
        NoteModel(StringName.STRINGDEFAULT, true, R.drawable.a_, "A2-110.00 Hz"),
        NoteModel(StringName.STRINGDEFAULT, true, R.drawable.e_, "E2-82.41 Hz")
                                                      )
}