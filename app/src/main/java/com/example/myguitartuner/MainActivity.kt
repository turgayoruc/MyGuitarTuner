package com.example.myguitartuner


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts//Microfon Permission islemlerini disaridaki bir class ile yapamadim.Ilerde bakarsin
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

import androidx.compose.ui.Modifier


//import androidx.compose.ui.graphics.RenderEffect//Bu yanlis olan
import androidx.core.content.ContextCompat


import com.example.myguitartuner.d_ui_katmani.a_theme.MyGuitarTunerTheme
import com.example.myguitartuner.d_ui_katmani.d_intents.TunerIntent
import com.example.myguitartuner.d_ui_katmani.e_viewModels.TunerViewModel
import com.example.myguitartuner.d_ui_katmani.screens.MainScreen


import kotlin.getValue

class MainActivity : ComponentActivity() {

    val viewModel: TunerViewModel by viewModels()//Bu kisimlar icin Hilt kullanilacak
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("toruc", "mic oncesi")
        checkMicrophonePermission()
        setContent {
            MyGuitarTunerTheme {
                Scaffold() { innerPadding ->
                    MainScreen(name = "Android", modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }

    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // viewModel.startTuning()

                // viewModel.updateAlertMesaji("izni onayladiniz")
                //   checkMicrophonePermission()//Bu tekrar check islemii aliskanlik haline getir. Cunku izne bagli metolar check islemine gore calisabiliyor sanki. Check yapmadan direkt baslatirsan uygulama cokuyor.
                // viewModel.startTuning()//Birda olmaz cunku bu metoda gidersen izne tabioldugunu gorursun.O yuzden burada tekrar check islemini yapiyorum.
            } else {
                // Kullanıcı izni reddetti
            }
        }
    //Burasi sadece izinlerin kaydini kontrol ediyor, kayit durumuna gore istedigin metodu baslatirsin. Izin isteme ekranini requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO) aciyor ve sonucu da kaydediyor.
    private fun checkMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {

                viewModel.onIntent(TunerIntent.StartEngine)


            }

            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {

                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)//ilk istege onay verilmediyse ikinci baslatmada da bi yeniistek iyi olur.
            }

            else -> {

                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)//Izin isteme ekranini  aciyor ve sonucu da kaydediyor.(While using the app) ekranini aciyor yani
            }
        }
    }

}



