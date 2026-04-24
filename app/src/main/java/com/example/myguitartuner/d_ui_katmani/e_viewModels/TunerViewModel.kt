package com.example.myguitartuner.d_ui_katmani.e_viewModels

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myguitartuner.b_data_katmani.a_audio.AudioEngine
import com.example.myguitartuner.d_ui_katmani.d_intents.TunerIntent
import com.example.myguitartuner.d_ui_katmani.c_states.TunerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TunerViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    //val audioEngine: MyAudioEngine//Bu this ile isi cozdum. Bu callback sayesinde MyAudioEngine classinda viewModel nesnesi olusturmama gerek kalmayacak. Yani nesne olusumunu garip bir sekilde tersten yapiyoruz.
    private val _state = MutableStateFlow<TunerUiState>(TunerUiState())
    val state = _state.asStateFlow()//Baska class buradaki yapiya disaridan mudahale edememsi icin. yani veri tek yonlu aklsin diye bunu yaptik.MVI mimarisi icin yani.


   private val audioEngine = AudioEngine()
    init {observePitch()}

    //Engine icindenki pitch verisi henuz state degil. Onu bir fonskiyon yardimiyla
    //State'lerimiz icindeki pitch degerine aktariyoruz. Yani bu fonsiyon ile Engine verisini State'e donusturduk.
    //Neden observePitch() ViewModel İçinde Olmalı?
    //observePitch fonksiyonu bir köprüdür. Repository'den gelen ham veriyi (Data), ViewModel'in işleyebileceği bir aksiyona (Intent) dönüştürür.
    //Akış şöyledir: Repository (Flow) -> ViewModel (Observe) -> Intent -> State Update
    //Bu akışta veriyi dinlemeye başlayan ve viewModelScope kullanarak bu dinlemeyi yöneten yer ViewModel olmalıdır. Çünkü ViewModel yok olduğunda bu dinleme (mikrofon takibi) otomatik olarak durmalıdır.
    private fun observePitch() {
        audioEngine.rawPitchFlow.conflate() // Mikrofon çok hızlıysa aradaki verileri atla, en günceli al
                .onEach { rawPitch ->
                    // MVI'da state güncelleme
                    // _state.update { it.copy(pitch = pitchHz.toInt()) }//Bunu kullnirsan MVI mimarisinden cikmis olursun cunku bu kod State guncellemesini
                    // Intent kullanmadan yapmaya calisiyor. Bu birden fazla ksiinin state e mudahale etmesi demek bu daMVI mimarisine aykiri.
                    // Senin şu anki observePitch fonksiyonun, onIntent kapısını kullanmadan arka kapıdan sızıp State'i değiştiriyor.
                    //O yuzden observePitch() fanksiyonu yine calisacak, yine init icinde calistiracagiz ama pitch state'i kendisi guncellemeyecek onu intent icindeki bir intente yaptiracagiz.
                    onIntent(TunerIntent.UpdateStateOfPitchYuzdeRenk(rawPitch))
                }.launchIn(viewModelScope)
    }

    //Engine islemleri ustte bitti simdi sira State'ler ile Intent'leri birlistirmede.
    //Buradaki yapi Composable tarafindan cagrilacak.

    fun onIntent(intent: TunerIntent) {
        when (intent) {
            is TunerIntent.StartEngine                 -> {
                audioEngine.startTuningInEngine(viewModelScope)
            }
            // 1. Mikrofondan ses geldiğinde (Tetikleyici: observePitch)
            //Engine'dan gelen raw yani ham pitch degerini, yuzdehesabinin sonucunu ve %100 e yaklastikca olacak olan yesil rengin state e atamalarini burada yapiyoruz.
            //State'e atadigim pitch degerini suan kullnmiyorum ama ileride lazim olabilir diye sakliyorum.Simdili bana yuzde ve renk yetiyor.Zaten onlari rawPitch ile yani ham pitch degeri ile hesapladigim icin state'deki pitch degerine ihhtiyacim yok.
            is TunerIntent.UpdateStateOfPitchYuzdeRenk -> {
                _state.update { currentState ->
                    val currentPitch = intent.pitch//yenisi
                    val target = currentState.target//eskisi

                    // Eğer target 0 ise bölme hatası almamak için kontrol ekleyelim
                    if (target == 0.0) {
                        currentState.copy(pitch = currentPitch.toInt())
                    } else {
                        // Hesaplamaları yapıyoruz
                        val difference = Math.abs(currentPitch - target)
                        val hesaplananYuzde = ((1 - difference / target) * 100).coerceIn(0.0, 100.0).toInt()

                        // Renk mantığı: Fark hedef frekansın %1'inden azsa yeşil yap (hassasiyeti buradan ayarlayabilirsin)
                        val yeniRenk = if (difference <= target * 0.01) Color.Companion.Green else Color.Companion.Gray

                        // TEK SEFERDE tüm bağlı verileri güncelliyoruz
                        currentState.copy(
                                pitch = currentPitch.toInt(),
                                yuzde = hesaplananYuzde,
                                renk = yeniRenk
                                         )
                    }
                }
            }

            // 2. Kullanıcı bir tele tıkladığında hedef frekans değişir
            is TunerIntent.UpdateTarget                -> {
                _state.update { it.copy(target = intent.target) }
            }

            is TunerIntent.UpdateHighlighting          -> {
                _state.update { currentState ->
                    val selected = intent.selectedItem
                    val name = selected.telAdi

                    when (name) {
                        "STRING1" -> currentState.copy(string1 = currentState.string1.map { it.copy(highlighting = it == selected) })
                        "STRING2" -> currentState.copy(string2 = currentState.string2.map { it.copy(highlighting = it == selected) })
                        "STRING3" -> currentState.copy(string3 = currentState.string3.map { it.copy(highlighting = it == selected) })
                        "STRING4" -> currentState.copy(string4 = currentState.string4.map { it.copy(highlighting = it == selected) })
                        "STRING5" -> currentState.copy(string5 = currentState.string5.map { it.copy(highlighting = it == selected) })
                        "STRING6" -> currentState.copy(string6 = currentState.string6.map { it.copy(highlighting = it == selected) })
                        else -> currentState
                    }

                }
            }

            // 3. String (Tel) listelerini güncelleme
            is TunerIntent.UpdateString1               -> _state.update { it.copy(string1 = intent.string1) }
            is TunerIntent.UpdateString2               -> _state.update { it.copy(string2 = intent.string2) }
            is TunerIntent.UpdateString3               -> _state.update { it.copy(string3 = intent.string3) }
            is TunerIntent.UpdateString4               -> _state.update { it.copy(string4 = intent.string4) }
            is TunerIntent.UpdateString5               -> _state.update { it.copy(string5 = intent.string5) }
            is TunerIntent.UpdateString6               -> _state.update { it.copy(string6 = intent.string6) }

            is TunerIntent.UpdateSelectedAllString     -> _state.update { currentState ->
                val selectedItem = intent.selectedItem
                when (selectedItem.telAdi) {
                    "STRING1" -> currentState.copy(selectedBirinci = selectedItem)
                    "STRING2" -> currentState.copy(selectedIkinci = selectedItem)
                    "STRING3" -> currentState.copy(selectedUcuncu = selectedItem)
                    "STRING4" -> currentState.copy(selectedDorduncu = selectedItem)
                    "STRING5" -> currentState.copy(selectedBesinci = selectedItem)
                    "STRING6" -> currentState.copy(selectedAltinci = selectedItem)
                    // Eğer başka durumlar da varsa ama state değişmesin istiyorsan:
                    else -> currentState
                }
            }


            // 4. Seçili nota listelerini güncelleme
            is TunerIntent.UpdateSelectedBirinci       -> _state.update { it.copy(selectedBirinci = intent.selectedBirinci) }
            is TunerIntent.UpdateSelectedIkinci        -> _state.update { it.copy(selectedIkinci = intent.selectedIkinci) }
            is TunerIntent.UpdateSelectedUcuncu        -> _state.update { it.copy(selectedUcuncu = intent.selectedUcuncu) }
            is TunerIntent.UpdateSelectedDorduncu      -> _state.update { it.copy(selectedDorduncu = intent.selectedDorduncu) }
            is TunerIntent.UpdateSelectedBesinci       -> _state.update { it.copy(selectedBesinci = intent.selectedBesinci) }
            is TunerIntent.UpdateSelectedAltinci       -> _state.update { it.copy(selectedAltinci = intent.selectedAltinci) }

            // 5. Aktif tel bilgisini güncelleme
            is TunerIntent.UpdateActiveString          -> _state.update { it.copy(activeString = intent.activeString) }

            // 6. Henüz içi boş olanlar
            is TunerIntent.LoadSavedSettings           -> { /* Veritabanı işlemleri burada tetiklenecek */
            }

            // Bu iki intent artık UpdatePitch içinde otomatikleştiği için
            // manuel çağırmana gerek kalmayabilir ama yine de bulunsunlar:
            //Zaten Intent.UpdateStateOfPitchYuzdeRenk ile yuzdeyi de renki de guncelliyorum.Bunlar ham yani raw pitch degerine bagli oldugu icin direkt state.copy islemini yaptim zaten.
//            is Intent.UpdateYuzde            -> _state.update { it.copy(yuzde = intent.yuzde) }
//            is Intent.UpdateRenk             -> _state.update { it.copy(renk = intent.renk) }

            is TunerIntent.UpdateScreenWidth           -> {
                _state.update { it.copy(screenWidthDp = intent.widthDp) }
            }

            is TunerIntent.UpdatePopupAcikMi           -> {
                _state.update { it.copy(popupAcikMi = intent.popupAcikMi) }
            }

            else -> {}
        }
    }


    private fun loadSettings() {
        viewModelScope.launch {
            // Burada Room database veya DataStore çağrılarını yapabilirsin
            // Örnek: val savedPitch = repository.getPitch()
            // _state.update { it.copy(pitch = savedPitch) }
        }
    }


//    fun updateHighlighting(item: MyNoteDataClass) {
//        MyEnumString.updateActiveString(
//                item, string1.value, string2.value, string3.value, string4.value, string5.value, string6.value
//                                       )
//    }

//    fun saveNotesinViewModelTryCatch(item: MyNoteDataClass) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val context = getApplication<Application>().applicationContext
//            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//
//            // KRİTİK DÜZELTME: LiveData'nın kendisini değil, .value (listeyi) alıyoruz
//            // Eğer activeString bir Liste ise:
//            val listToSave = when (item.nameEnum) {
//                MyEnumString.STRING1 -> string1.value ?: emptyList()
//                MyEnumString.STRING2 -> string2.value ?: emptyList()
//                MyEnumString.STRING3 -> string3.value ?: emptyList()
//                MyEnumString.STRING4 -> string4.value ?: emptyList()
//                MyEnumString.STRING5 -> string5.value ?: emptyList()
//                MyEnumString.STRING6 -> string6.value ?: emptyList()
//                else                 -> emptyList()
//            }
//            val json = Gson().toJson(listToSave) // Bu artık [...] şeklinde bir JSON üretir
//
//            val key = when (item.nameEnum) {
//                MyEnumString.STRING1 -> "string1"
//                MyEnumString.STRING2 -> "string2"
//                MyEnumString.STRING3 -> "string3"
//                MyEnumString.STRING4 -> "string4"
//                MyEnumString.STRING5 -> "string5"
//                MyEnumString.STRING6 -> "string6"
//                else                 -> null
//            }
//
//            key?.let {
//                sharedPreferences.edit().putString(it, json).apply()
//            }
//
//            //Sev isleminden sonra son kayit edileni tekrar alip tum selectedBrinci degerlerini goncellemeliyiz.
//            val sonuc1 = loadNotesinViewModelTryCatch("string1", string1.value, { _selectedBirinci.value = it })
//            val sonuc2 = loadNotesinViewModelTryCatch("string2", string2.value, { _selectedIkinci.value = it })
//            val sonuc3 = loadNotesinViewModelTryCatch("string3", string3.value, { _selectedUcuncu.value = it })
//            val sonuc4 = loadNotesinViewModelTryCatch("string4", string4.value, { _selectedDorduncu.value = it })
//            val sonuc5 = loadNotesinViewModelTryCatch("string5", string5.value, { _selectedBesinci.value = it })
//            val sonuc6 = loadNotesinViewModelTryCatch("string6", string6.value, { _selectedAltinci.value = it })
//            withContext(Dispatchers.Main) {
//                string1.postValue(sonuc1)
//                string2.postValue(sonuc2)
//                string3.postValue(sonuc3)
//                string4.postValue(sonuc4)
//                string5.postValue(sonuc5)
//                string6.postValue(sonuc6)
//            }
//
//        }
//    }
//
//    suspend fun loadNotesinViewModelTryCatch(
//            keyName: String,
//            nullGelirse: List<MyNoteDataClass>,
//            onSelectAction: (MyNoteDataClass) -> Unit,
//                                            ): List<MyNoteDataClass> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val context = getApplication<Application>().applicationContext
//                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//                val json = sharedPreferences.getString(keyName, null) ?: return@withContext nullGelirse
//
//                // JSON'un bir array olup olmadığını basitçe kontrol edelim
//                if (!json.trim().startsWith("[")) {
//                    Log.e("GSON_ERROR", "Beklenen liste formatı bulunamadı, gelen veri: $json")
//                    return@withContext nullGelirse
//                }
//
//                val type = object : TypeToken<ArrayList<MyNoteDataClass>>() {}.type
//                val notes: List<MyNoteDataClass> = Gson().fromJson(json, type)
//
//                notes.onEach { it.onSelected = onSelectAction }
//                return@withContext notes
//            } catch (e: Exception) {
//                // Herhangi bir parse hatasında (BEGIN_OBJECT hatası dahil) buraya düşer
//                Log.e("GSON_ERROR", "Parse edilemedi: ${e.message}")
//                return@withContext nullGelirse // Uygulama çökmez, varsayılan listeyi döner
//            }
//        }
 //   }

//    //SharedPreferences kullanarak bu isi hallettim.
//    suspend fun beginningOfStrings() {
//        val sonuc1 = loadNotesinViewModelTryCatch("string1", string1.value, { _selectedBirinci.value = it })
//        val sonuc2 = loadNotesinViewModelTryCatch("string2", string2.value, { _selectedIkinci.value = it })
//        val sonuc3 = loadNotesinViewModelTryCatch("string3", string3.value, { _selectedUcuncu.value = it })
//        val sonuc4 = loadNotesinViewModelTryCatch("string4", string4.value, { _selectedDorduncu.value = it })
//        val sonuc5 = loadNotesinViewModelTryCatch("string5", string5.value, { _selectedBesinci.value = it })
//        val sonuc6 = loadNotesinViewModelTryCatch("string6", string6.value, { _selectedAltinci.value = it })
//
//        withContext(Dispatchers.IO) {
//            string1.postValue(sonuc1)
//            string2.postValue(sonuc2)
//            string3.postValue(sonuc3)
//            string4.postValue(sonuc4)
//            string5.postValue(sonuc5)
//            string6.postValue(sonuc6)
//        }
//    }
}