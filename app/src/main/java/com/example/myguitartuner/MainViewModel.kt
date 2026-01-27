package com.example.myguitartuner


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import kotlin.compareTo
import kotlin.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.myguitartuner.AudioEngine
import com.example.myguitartuner.ui.theme.MyGuitarTunerTheme
import java.lang.reflect.Method


class MainViewModel : ViewModel() {

    val alertMesaji: MutableLiveData<String>

    val audioEngine : MyAudioEngine//Bu this ile isi cozdum. Bu callback sayesinde MyAudioEngine classinda viewModel nesnesi olusturmama gerek kalmayacak. Yani nesne olusumunu garip bir sekilde tersten yapiyoruz.
    val pitch: MutableLiveData<Int>

    val buffer: MutableLiveData<String>



    init {
        alertMesaji = MutableLiveData<String>("")
        pitch = MutableLiveData<Int>(0)
        buffer= MutableLiveData<String>("34")
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

//    override fun onCleared() {
//        audioEngine.stop()
//    }

}

