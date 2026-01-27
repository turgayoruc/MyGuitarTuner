package com.example.myguitartuner

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder

class AudioEngine {

    private  val SAMPLE_RATE = 44100
    private  val BUFFER_SIZE = 2048


    private val minBuffer =
        AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

    @SuppressLint("MissingPermission")//Ben zate izni kontrol ediyorum
    private val audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        maxOf(minBuffer, BUFFER_SIZE)
    )

    fun start(onAudio: (ShortArray) -> Unit) {
        val buffer = ShortArray(BUFFER_SIZE)
        audioRecord.startRecording()

        Thread {
            while (audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = audioRecord.read(buffer, 0, buffer.size)
                if (read > 0) {
                    onAudio(buffer.copyOf(read))
                }
            }
        }.start()
    }

    fun stop() {
        audioRecord.stop()
        audioRecord.release()
    }

    fun shortToFloat(input: ShortArray): FloatArray {
        return FloatArray(input.size) { i ->
            input[i] / 32768f
        }
    }

    fun difference(signal: FloatArray, bufferSize: Int): FloatArray {
        val diff = FloatArray(bufferSize / 2)

        for (tau in 1 until diff.size) {
            var sum = 0f
            for (i in 0 until diff.size) {
                val delta = signal[i] - signal[i + tau]
                sum += delta * delta
            }
            diff[tau] = sum
        }
        return diff
    }

    fun cumulativeMeanNormalizedDifference(diff: FloatArray): FloatArray {
        val cmnd = FloatArray(diff.size)
        var runningSum = 0f
        cmnd[0] = 1f

        for (tau in 1 until diff.size) {
            runningSum += diff[tau]
            cmnd[tau] = diff[tau] * tau / runningSum
        }
        return cmnd
    }

    fun absoluteThreshold(
        cmnd: FloatArray,
        threshold: Float = 0.1f
    ): Int {

        var tau = 2
        while (tau < cmnd.size) {

            if (cmnd[tau] < threshold) {
                while (
                    tau + 1 < cmnd.size &&
                    cmnd[tau + 1] < cmnd[tau]
                ) {
                    tau++
                }
                return tau
            }

            tau++
        }

        return -1
    }

    fun getPitch(signal: FloatArray): Float {
        val diff = difference(signal, signal.size)
        val cmnd = cumulativeMeanNormalizedDifference(diff)
        val tau = absoluteThreshold(cmnd)

        return if (tau != -1) {
            SAMPLE_RATE / tau.toFloat()
        } else {
            -1f
        }
    }


}