package com.example.myguitartuner

import android.R
import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.DoneSegment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAudioEngine(private val viewModel: MainViewModel) {

    private lateinit var audioRecord: AudioRecord
    val sampleRate = 44100

    fun startTuning() {

        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate, // sampleRateInHz
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT//Ham veriyi kullanacagimizi soyluyoruz.
        )
        @SuppressLint("MissingPermission")//Ben zaten izni kontrol ediyorum
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100, // Sample rate (CD quality)
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
        //Microfonu aktif hale getiriyor ama bir veriyi alip da bir yere kaydetmiyor.O isi Thread ile yapiyoruz.
        audioRecord.startRecording()
        // Gelen sesi kaydedecegimiz tampon bolgemizi onceden ayarliyoruz. Biz buraya read yardimiyla analog sesi okuyup onceden ayarladigimiz ayarlarda digital verisini cekecegiz.
        val audioBuffer = ShortArray(bufferSize)

        CoroutineScope(Dispatchers.IO).launch{
            while (isActive){
                var readSize=0
                readSize= audioRecord.read(audioBuffer, 0, bufferSize)//metod geri döndüğü anda okuma işlemi bitmiştir.yani resultun ilk atanan deger olmamasi iyi bir kontrol.
                if (readSize == bufferSize){  //readSize > 0 ise → veri hazırdır
                    val signal = DoubleArray(bufferSize)
                    for (i in audioBuffer.indices) {
                        signal[i] = audioBuffer[i].toDouble()
                    }
                    removeDC(signal)
                    val pitchHz = autoCorrelate(signal, sampleRate)

                    withContext(Dispatchers.Main){
                        //  viewModel.updatePitch(pitchHz.toInt())
                        ////viewModel.updateAudioBuffer(pitchHz.toString())
                      for (i in 0..100){
                          delay(500)
                          viewModel.updatePitch(10*i.toInt())}
                    }
                }
            }

        }
    }
    fun removeDC(signal: DoubleArray) {
        val mean = signal.average()
        for (i in signal.indices) {
            signal[i] -= mean
        }
    }


    fun autoCorrelate(signal: DoubleArray, sampleRate: Int): Double {
        val size = signal.size
        var bestLag = 0
        var bestCorr = 0.0

        // Gitar frekansları için lag sınırı
        val minLag = sampleRate / 1200   // ~36
        val maxLag = sampleRate / 80     // ~551

        for (lag in minLag..maxLag) {
            var sum = 0.0
            for (i in 0 until size - lag) {
                sum += signal[i] * signal[i + lag]
            }

            if (sum > bestCorr) {
                bestCorr = sum
                bestLag = lag
            }
        }

        return if (bestLag == 0) 0.0 else sampleRate.toDouble() / bestLag
    }












}