package com.example.myguitartuner.data.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AudioTunerEngine {

    // Veriyi dışarıya akıtacak olan Flow. MVI icin normal callBack'i sildik. yerine callbackFollow kullnacagiz.
    private val _rawPitchFlow = MutableSharedFlow<Double>(replay = 0)
    val rawPitchFlow = _rawPitchFlow.asSharedFlow()

    private lateinit var audioRecord: AudioRecord
    val sampleRate = 44100


    fun startTuningInEngine(scope: CoroutineScope) {

       // val bufferSize=8192
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

        scope.launch(Dispatchers.IO){
            while (isActive){
                var readSize=0
                readSize= audioRecord.read(audioBuffer, 0, bufferSize)//metod geri döndüğü anda okuma işlemi bitmiştir.yani resultun ilk atanan deger olmamasi iyi bir kontrol.
                if (readSize == bufferSize){  //readSize > 0 ise → veri hazırdır
                    val signal = DoubleArray(bufferSize)
                    for (i in audioBuffer.indices) {
                        signal[i] = audioBuffer[i].toDouble()
                    }
                    removeDC(signal)
                    val pitch = autoCorrelate(signal, sampleRate)
                    _rawPitchFlow.emit(pitch) //view'e gitmak yerine Flow ile veriyi firlatacagimiz icin ayriyetten withContext(Dispatchers.Main)'e ihtiyacimiz yok.
//                    withContext(Dispatchers.Main){
//
//                        //MVI mimarisi icin yani tek yonlu mimari icin viewModel'e uradan erisim saglamayacagiz. ViewModel burdan veriyi dinleyecek.
//                          //viewModel.updatePitch(pitch.toInt())
//                       // viewModel.updateAudioBuffer(pitchHz.toString())
////                      for (i in 0..200){
////                          delay(50)
////                          viewModel.updatePitch(i.toInt())}
//                    }
                }
            }

        }
    }
    fun stopEngine() {
        audioRecord.stop()
        audioRecord.release()
       // audioRecord = null
    }

    fun removeDC(signal: DoubleArray) {
        val mean = signal.average()
        for (i in signal.indices) {
            signal[i] -= mean
        }
    }

    var sonGecerliDeger=0.0
    fun autoCorrelate(signal: DoubleArray, sampleRate: Int): Double {
        val size = signal.size

        // 1. ADIM: Ses Seviyesi Kontrolü (Gürültü Kapısı / Noise Gate)
        // Sinyalin ortalama mutlak değerine bakalım.
        var sumAbs = 0.0
        for (s in signal) sumAbs += Math.abs(s)
        val averageAbs = sumAbs / size

        // Eğer ses seviyesi çok düşükse (sessiz ortam), işlemi direkt iptal et.
        // 500.0 değerini cihazına göre 200 ile 1000 arasında test ederek ayarlayabilirsin.
        if (averageAbs < 500.0) return sonGecerliDeger

        var bestLag = -1
        var maxCorr = -1.0

        val minLag = sampleRate / 1000 // Üst sınır (1000Hz)
        val maxLag = sampleRate / 30   // Alt sınır (70Hz - Gitarın en kalın teli için)

        // 2. ADIM: Korelasyon Hesaplama
        for (lag in minLag..maxLag) {
            var corr = 0.0
            for (i in 0 until size - lag) {
                corr += signal[i] * signal[i + lag]
            }

            if (corr > maxCorr) {
                maxCorr = corr
                bestLag = lag
            }
        }

        // 3. ADIM: Kalite Kontrolü (Confidence)
        // Sinyalin sıfır kaydırmadaki (lag=0) kendi enerjisiyle karşılaştırıyoruz.
        var energy = 0.0
        for (i in 0 until size) energy += signal[i] * signal[i]

        val confidence = if (energy > 0) maxCorr / energy else 0.0
        if (confidence > 0.70 && bestLag != -1) {
           sonGecerliDeger= sampleRate.toDouble() / bestLag}
        // Gitar notası için confidence genelde 0.85 ve üzeridir.
        // Gürültüde bu oran çok düşer. 0.70 güvenli bir sınır.
        return if (confidence > 0.70 && bestLag != -1) {sonGecerliDeger} else { 0.0 }
    }












}