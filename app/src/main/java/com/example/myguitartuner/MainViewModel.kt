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





    init {
        alertMesaji = MutableLiveData<String>("")
        pitch = MutableLiveData<Int>(0)
        buffer= MutableLiveData<String>("34")
        notaFrekansi= MutableLiveData<Int>(0)
        yuzdesi= MutableLiveData<Int>(0)
        renk= MutableLiveData<Color>(kremRengi)
        audioEngine = MyAudioEngine(this)
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

//    fun updateYuzde(target:Int, ){
//        var pairOfProximity=calculateProximity(target,pitch.value.toFloat())
//        yuzdeSonuc.value=pairOfProximity.first.toInt()
//        renkKodu.value=pairOfProximity.second
//        var textColor = if (renkKodu.value==1) Color.Green else kremRengi
//    }

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

//    override fun onCleared() {
//        audioEngine.stop()
//    }

}

