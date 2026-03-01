package com.example.myguitartuner


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import androidx.compose.ui.graphics.Color
import com.example.myguitartuner.ui.theme.kremRengi


class MainViewModel : ViewModel() {

    val alertMesaji: MutableLiveData<String>

    val audioEngine : MyAudioEngine//Bu this ile isi cozdum. Bu callback sayesinde MyAudioEngine classinda viewModel nesnesi olusturmama gerek kalmayacak. Yani nesne olusumunu garip bir sekilde tersten yapiyoruz.
    val pitch: MutableLiveData<Int>

    val buffer: MutableLiveData<String>
    var notaFrekansi: MutableLiveData<Int>
    var yuzdesi: MutableLiveData<Int>
    var renk: MutableLiveData<Color>

    val selectedBirinci: MutableLiveData<MyNoteDataClass>
    val selectedIkinci: MutableLiveData<MyNoteDataClass>
    val selectedUcuncu: MutableLiveData<MyNoteDataClass>
    val selectedDorduncu: MutableLiveData<MyNoteDataClass>
    val selectedBesinci: MutableLiveData<MyNoteDataClass>
    val selectedAltinci: MutableLiveData<MyNoteDataClass>

    val string1 = listOf(
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.c_, "C4-261.63 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.d_, "D4-293.66 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.ddiyez_, "D♯4 / E♭4-311.13 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,true,R.drawable.e_, "E4-329.63 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.f_, "F4-349.23 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.f_, "F♯4 / G♭4-369.99 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING1,false,R.drawable.g_, "G4-392.00 Hz", { selectedBirinci.value = it })
    )
    val string2 = listOf(
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.g_, "G3-196.00 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.a_, "A3-220.00 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,true,R.drawable.b_, "B3-246.94 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.c_, "C4-261.63 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING2,false,R.drawable.d_, "D4-293.66 Hz", { selectedIkinci.value = it })
    )
    val string3 = listOf(
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.ddiyez_, "D♯3 / E♭3-155.56 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.e_, "E3-164.81 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.f_, "F3-174.61 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,true,R.drawable.g_, "G3-196.00 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.a_, "A3-220.00 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(MyEnumString.STRING3,false,R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz") { selectedUcuncu.value = it }
    )
    val string4 = listOf(
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.adiyez_, "B♭2-116.54 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.b_, "B2-123.47 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.c_, "C3-130.81 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,true, R.drawable.d_, "D3-146.83 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.e_, "E3-164.81 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.f_, "F3-174.61 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRING4,false,R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz", { selectedDorduncu.value = it })
    )
    val string5 = listOf(
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.f_, "F2-87.31 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.fdiyez_, "F♯2 / G♭2-92.50 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.g_, "G2-98.00 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.gdiyez_, "G♯2 / A♭2-103.83 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,true,R.drawable.a_, "A2-110.00 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.b_, "B2-123.47 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.c_, "C3-130.81 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING5,false,R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz", { selectedBesinci.value = it })
    )
    val string6 = listOf(
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.c_, "C2-65.41 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.cdiyez_, "C♯2 / D♭2-69.30 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.d_, "D2-73.42 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.ddiyez_, "D♯2 / E♭2-77.78 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,true, R.drawable.e_, "E2-82.41 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.f_, "F2-92.50 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.fdiyez_, "F♯2 / G♭2-98.00 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(MyEnumString.STRING6,false,R.drawable.g_, "G2-103.83 Hz", { selectedAltinci.value = it })
    )
    val stringDefault = listOf(
        MyNoteDataClass(MyEnumString.STRINGDEFAULT,true,R.drawable.e_, "E4-329.63 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(MyEnumString.STRINGDEFAULT,true,R.drawable.b_, "B3-246.94 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(MyEnumString.STRINGDEFAULT,true,R.drawable.g_, "G3-196.00 Hz", { selectedUcuncu.value = it }),
        MyNoteDataClass(MyEnumString.STRINGDEFAULT,true,R.drawable.d_, "D3-146.83 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(MyEnumString.STRINGDEFAULT,true,R.drawable.a_, "A2-110.00 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(MyEnumString.STRINGDEFAULT,true,R.drawable.e_, "E2-82.41 Hz", { selectedAltinci.value = it })
    )

    val activeString: MutableLiveData<List<MyNoteDataClass>>

    init {
        alertMesaji = MutableLiveData<String>("")
        pitch = MutableLiveData<Int>(0)
        buffer= MutableLiveData<String>("34")
        notaFrekansi= MutableLiveData<Int>(0)
        yuzdesi= MutableLiveData<Int>(0)
        renk= MutableLiveData<Color>(kremRengi)
        audioEngine = MyAudioEngine(this)

        selectedBirinci= MutableLiveData<MyNoteDataClass>(string1.getOrNull(4))
        selectedIkinci= MutableLiveData<MyNoteDataClass>(string2.getOrNull(4))
        selectedUcuncu= MutableLiveData<MyNoteDataClass>(string3.getOrNull(4))
        selectedDorduncu= MutableLiveData<MyNoteDataClass>(string4.getOrNull(4))
        selectedBesinci= MutableLiveData<MyNoteDataClass>(string5.getOrNull(4))
        selectedAltinci= MutableLiveData<MyNoteDataClass>(string6.getOrNull(4))

        activeString= MutableLiveData<List<MyNoteDataClass>>(string1)
    }


    fun updateAlertMesaji(mesaj: String) {
        this.alertMesaji.value = mesaj
    }

    fun updatePitch(pitch: Int) {
       // this.pitch.value = pitch
        this.pitch.postValue(pitch)
    }

    fun updateAudioBuffer(message:String){
        this.buffer.postValue(message)//postValue disindakielri de dene
    }

    fun startTuning() {
        audioEngine.startTuning()
    }
    //target: Gitar telinin olmasi gereken frekansi
    //value: Gitar telinin olmasi gereken frekansi
    fun updateYuzdeVeRenk(target: Int, pitch: Int) {
        val difference = Math.abs(pitch - target)
        // Yakınlık yüzdesini hesapla: (1 - fark / hedef) * 100
        yuzdesi.value = ((1 - difference / target.toFloat()) * 100).coerceIn(0f, 100f).toInt()
        // Eğer fark %10'dan küçükse 1, diğer durumda 0 döner
        val renkKodu= if (difference <= target * 0.1f) 1 else 0
        renk.value = if (renkKodu==1) Color.Green else kremRengi

    }

    fun updateNotaFrekansi(notaFrekansi: Int){
        // this.pitch.value = pitch
        this.notaFrekansi.postValue(notaFrekansi)
    }

    fun updateActiveString(newString: List<MyNoteDataClass>){
        activeString.value=newString
    }
    fun updateHighlighting(item: MyNoteDataClass){

        MyEnumString.updateActiveString(item,string1, string2,string3,string4,string5,string6)


    }


}

