package com.example.myguitartuner

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.compose.ui.graphics.Color
import com.example.myguitartuner.ui.theme.kremRengi

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import androidx.core.content.edit // Bu import'un olduğundan emin olun
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.forEach

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val alertMesaji: MutableLiveData<String>

    val audioEngine: MyAudioEngine//Bu this ile isi cozdum. Bu callback sayesinde MyAudioEngine classinda viewModel nesnesi olusturmama gerek kalmayacak. Yani nesne olusumunu garip bir sekilde tersten yapiyoruz.
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

    val string1 = MutableLiveData(listOf(
                    MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.c_, "C4-261.63 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.d_, "D4-293.66 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.ddiyez_, "D♯4 / E♭4-311.13 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, true, R.drawable.e_, "E4-329.63 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.f_, "F4-349.23 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.f_, "F♯4 / G♭4-369.99 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING1, false, R.drawable.g_, "G4-392.00 Hz", {
        selectedBirinci.value = it
    })))
    val string2 = MutableLiveData(listOf(
                    MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.g_, "G3-196.00 Hz", {
        selectedIkinci.value = it
    }),
                                         MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.a_, "A3-220.00 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING2, true, R.drawable.b_, "B3-246.94 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.c_, "C4-261.63 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING2, false, R.drawable.d_, "D4-293.66 Hz", {
        selectedIkinci.value = it
    })))
    val string3 = MutableLiveData(listOf(
                    MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.ddiyez_, "D♯3 / E♭3-155.56 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.e_, "E3-164.81 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.f_, "F3-174.61 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, true, R.drawable.g_, "G3-196.00 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.a_, "A3-220.00 Hz") {
                selectedUcuncu.value = it
            }, MyNoteDataClass(MyEnumString.STRING3, false, R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz") {
                selectedUcuncu.value = it
            }))
    val string4 = MutableLiveData(listOf(MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.adiyez_, "B♭2-116.54 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.b_, "B2-123.47 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.c_, "C3-130.81 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, true, R.drawable.d_, "D3-146.83 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.e_, "E3-164.81 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.f_, "F3-174.61 Hz", {
                selectedDorduncu.value = it
            }), MyNoteDataClass(MyEnumString.STRING4, false, R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz", {
                selectedDorduncu.value = it
            })) )
    val string5 = MutableLiveData(listOf(MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.f_, "F2-87.31 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.fdiyez_, "F♯2 / G♭2-92.50 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.g_, "G2-98.00 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.gdiyez_, "G♯2 / A♭2-103.83 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, true, R.drawable.a_, "A2-110.00 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.b_, "B2-123.47 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.c_, "C3-130.81 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING5, false, R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz", {
        selectedBesinci.value = it
    })))
    val string6 = MutableLiveData(listOf(MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.c_, "C2-65.41 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.cdiyez_, "C♯2 / D♭2-69.30 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.d_, "D2-73.42 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.ddiyez_, "D♯2 / E♭2-77.78 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, true, R.drawable.e_, "E2-82.41 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.f_, "F2-92.50 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.fdiyez_, "F♯2 / G♭2-98.00 Hz", {
        selectedAltinci.value = it
    }), MyNoteDataClass(MyEnumString.STRING6, false, R.drawable.g_, "G2-103.83 Hz", {
        selectedAltinci.value = it
    })))
    val stringDefault = listOf(
            MyNoteDataClass(MyEnumString.STRINGDEFAULT, true, R.drawable.e_, "E4-329.63 Hz", {
        selectedBirinci.value = it
    }), MyNoteDataClass(MyEnumString.STRINGDEFAULT, true, R.drawable.b_, "B3-246.94 Hz", {
        selectedIkinci.value = it
    }), MyNoteDataClass(MyEnumString.STRINGDEFAULT, true, R.drawable.g_, "G3-196.00 Hz", {
        selectedUcuncu.value = it
    }), MyNoteDataClass(MyEnumString.STRINGDEFAULT, true, R.drawable.d_, "D3-146.83 Hz", {
        selectedDorduncu.value = it
    }), MyNoteDataClass(MyEnumString.STRINGDEFAULT, true, R.drawable.a_, "A2-110.00 Hz", {
        selectedBesinci.value = it
    }), MyNoteDataClass(MyEnumString.STRINGDEFAULT, true, R.drawable.e_, "E2-82.41 Hz", {
        selectedAltinci.value = it
    }))

    val activeString: MutableLiveData<List<MyNoteDataClass>>

    init {
        alertMesaji = MutableLiveData<String>("")
        pitch = MutableLiveData<Int>(0)
        buffer = MutableLiveData<String>("34")
        notaFrekansi = MutableLiveData<Int>(0)
        yuzdesi = MutableLiveData<Int>(0)
        renk = MutableLiveData<Color>(kremRengi)
        audioEngine = MyAudioEngine(this)

        selectedBirinci = MutableLiveData<MyNoteDataClass>(string1.value.getOrNull(4))
        selectedIkinci = MutableLiveData<MyNoteDataClass>(string2.value.getOrNull(4))
        selectedUcuncu = MutableLiveData<MyNoteDataClass>(string3.value.getOrNull(4))
        selectedDorduncu = MutableLiveData<MyNoteDataClass>(string4.value.getOrNull(4))
        selectedBesinci = MutableLiveData<MyNoteDataClass>(string5.value.getOrNull(4))
        selectedAltinci = MutableLiveData<MyNoteDataClass>(string6.value.getOrNull(4))

        activeString = MutableLiveData<List<MyNoteDataClass>>(
                string1.value
                                                             )//Ilk olusturulurken null yerine string1.value yaptim sadece.

        viewModelScope.launch(Dispatchers.IO) {
            beginningOfStrings()


        }
    }

    fun updateAlertMesaji(mesaj: String) {
        this.alertMesaji.value = mesaj
    }

    fun updatePitch(pitch: Int) {
        // this.pitch.value = pitch
        this.pitch.postValue(pitch)
    }

    fun updateAudioBuffer(message: String) {
        this.buffer.postValue(message)//postValue disindakielri de dene
    }

    fun startTuningInViewModel() {
        audioEngine.startTuningInEngine()
    }

    //target: Gitar telinin olmasi gereken frekansi
    //value: Gitar telinin olmasi gereken frekansi
    fun updateYuzdeVeRenk(
            target: Int,
            pitch: Int,
                         ) {
        val difference = Math.abs(pitch - target)
        // Yakınlık yüzdesini hesapla: (1 - fark / hedef) * 100
        yuzdesi.value = ((1 - difference / target.toFloat()) * 100).coerceIn(0f, 100f).toInt()
        // Eğer fark %10'dan küçükse 1, diğer durumda 0 döner
        val renkKodu = if (difference <= target * 0.1f) 1 else 0
        renk.value = if (renkKodu == 1) Color.Green else kremRengi
    }

    fun updateNotaFrekansi(notaFrekansi: Int) {
        // this.pitch.value = pitch
        this.notaFrekansi.postValue(notaFrekansi)
    }

    fun updateActiveString(newString: List<MyNoteDataClass>) {
        activeString.value = newString
    }

    fun updateHighlighting(item: MyNoteDataClass) {

        MyEnumString.updateActiveString(
                item, string1.value, string2.value, string3.value, string4.value, string5.value, string6.value
                                       )
    }

    fun saveNotesinViewModelTryCatch(item: MyNoteDataClass) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            // KRİTİK DÜZELTME: LiveData'nın kendisini değil, .value (listeyi) alıyoruz
            // Eğer activeString bir Liste ise:
            val listToSave = when (item.nameEnum) {
                MyEnumString.STRING1 -> string1.value ?: emptyList()
                MyEnumString.STRING2 -> string2.value ?: emptyList()
                MyEnumString.STRING3 -> string3.value ?: emptyList()
                MyEnumString.STRING4 -> string4.value ?: emptyList()
                MyEnumString.STRING5 -> string5.value ?: emptyList()
                MyEnumString.STRING6 -> string6.value ?: emptyList()
                else                 -> emptyList()
            }
            val json = Gson().toJson(listToSave) // Bu artık [...] şeklinde bir JSON üretir

            val key = when (item.nameEnum) {
                MyEnumString.STRING1 -> "string1"
                MyEnumString.STRING2 -> "string2"
                MyEnumString.STRING3 -> "string3"
                MyEnumString.STRING4 -> "string4"
                MyEnumString.STRING5 -> "string5"
                MyEnumString.STRING6 -> "string6"
                else                 -> null
            }

            key?.let {
                sharedPreferences.edit().putString(it, json).apply()
            }

            //Sev isleminden sonra son kayit edileni tekrar alip tum selectedBrinci degerlerini goncellemeliyiz.
            val sonuc1 = loadNotesinViewModelTryCatch("string1", string1.value, { selectedBirinci.value = it })
            val sonuc2 = loadNotesinViewModelTryCatch("string2", string2.value, { selectedIkinci.value = it })
            val sonuc3 = loadNotesinViewModelTryCatch("string3", string3.value, { selectedUcuncu.value = it })
            val sonuc4 = loadNotesinViewModelTryCatch("string4", string4.value, { selectedDorduncu.value = it })
            val sonuc5 = loadNotesinViewModelTryCatch("string5", string5.value, { selectedBesinci.value = it })
            val sonuc6 = loadNotesinViewModelTryCatch("string6", string6.value, { selectedAltinci.value = it })
            withContext(Dispatchers.Main) {
                string1.postValue(sonuc1)
                string2.postValue(sonuc2)
                string3.postValue(sonuc3)
                string4.postValue(sonuc4)
                string5.postValue(sonuc5)
                string6.postValue(sonuc6)
            }

        }
    }

    suspend fun loadNotesinViewModelTryCatch(
            keyName: String,
            nullGelirse: List<MyNoteDataClass>,
            onSelectAction: (MyNoteDataClass) -> Unit,
                                            ): List<MyNoteDataClass> {
        return withContext(Dispatchers.IO) {
            try {
                val context = getApplication<Application>().applicationContext
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val json = sharedPreferences.getString(keyName, null) ?: return@withContext nullGelirse

                // JSON'un bir array olup olmadığını basitçe kontrol edelim
                if (!json.trim().startsWith("[")) {
                    Log.e("GSON_ERROR", "Beklenen liste formatı bulunamadı, gelen veri: $json")
                    return@withContext nullGelirse
                }

                val type = object : TypeToken<ArrayList<MyNoteDataClass>>() {}.type
                val notes: List<MyNoteDataClass> = Gson().fromJson(json, type)

                notes.onEach { it.onSelected = onSelectAction }
                return@withContext notes
            } catch (e: Exception) {
                // Herhangi bir parse hatasında (BEGIN_OBJECT hatası dahil) buraya düşer
                Log.e("GSON_ERROR", "Parse edilemedi: ${e.message}")
                return@withContext nullGelirse // Uygulama çökmez, varsayılan listeyi döner
            }
        }
    }
   //SharedPreferences kullanarak bu isi hallettim.
    suspend fun beginningOfStrings(){
        val sonuc1 = loadNotesinViewModelTryCatch("string1", string1.value, { selectedBirinci.value = it})
        val sonuc2 = loadNotesinViewModelTryCatch("string2", string2.value, { selectedIkinci.value = it})
        val sonuc3 = loadNotesinViewModelTryCatch("string3", string3.value, { selectedUcuncu.value = it})
        val sonuc4 = loadNotesinViewModelTryCatch("string4", string4.value, { selectedDorduncu.value = it})
        val sonuc5 = loadNotesinViewModelTryCatch("string5", string5.value, { selectedBesinci.value = it})
        val sonuc6 = loadNotesinViewModelTryCatch("string6", string6.value, { selectedAltinci.value = it})

        withContext(Dispatchers.IO) {
            string1.postValue(sonuc1)
            string2.postValue(sonuc2)
            string3.postValue(sonuc3)
            string4.postValue(sonuc4)
            string5.postValue(sonuc5)
            string6.postValue(sonuc6)
        }
    }
}

