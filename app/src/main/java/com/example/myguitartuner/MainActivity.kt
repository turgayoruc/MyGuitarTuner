package com.example.myguitartuner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel


import com.example.myguitartuner.ui.theme.MyGuitarTunerTheme


import kotlin.getValue

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()   // Bu normal viewModel composable olmayan. Bunun icin ayriyetten kutuphanye ihtiyacin yok ama composable fnksiyon icinde goremzsin.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            MyGuitarTunerTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(24.dp)) { innerPadding ->
                    FrontEnd(name = "Android",modifier = Modifier.padding(innerPadding) )

                }
            }
        }
        Log.d("toruc","mic oncesi")
        checkMicrophonePermission()
    }




    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if (isGranted) {
            Log.d("toruc","mic izin istiyorsun ve kullnici isni veriyor")
            // Kullanıcı izni verdi
           // Toast.makeText(this, "🎤 Mikrofon izni verildi", Toast.LENGTH_SHORT).show()
            // Burada tuner fonksiyonunu başlatabilirsin (örneğin startTuning())
        } else {
            Log.d("toruc","mic kullanici izni reddetti ")
            // Kullanıcı izni reddetti
           // Toast.makeText(this, "❌ Mikrofon izni reddedildi", Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkMicrophonePermission() {
        when {
            // ✅ Zaten izin verilmişse
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.alertMesaji.value="mic izin verilmis"

                Log.d("toruc","mic izin verilmis yani artik frekans dinleme metodu calisabilir")

               //viewModel.startPitchDetection()

              //  Toast.makeText(this, "🎸 Mikrofon izni zaten verilmiş", Toast.LENGTH_SHORT).show()
                // Burada direkt tuner fonksiyonunu başlatabilirsin
            }

            // ❗ Kullanıcı daha önce reddettiyse (açıklama gösterebilirsin)
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
//                Log.d("toruc","mic izin verilmemis")
//                Toast.makeText(
//                    this,
//                    "Uygulamanın çalışması için mikrofon izni gereklidir.",
//                    Toast.LENGTH_LONG
//                ).show()
                viewModel.alertMesaji.value="mic izin verilmemis"
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            // 🔹 İlk defa izin iste
            else -> {
                viewModel.alertMesaji.value="mic ilk defa izin istenmis"
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

}

@Composable
fun FrontEnd( name: String, modifier: Modifier = Modifier) {
    //viewModel Nesnesi Olusturuldu
    var viewModel: MainViewModel= viewModel()//Kutuphanesini yuklemezsen gelmez
    //viewModeldeki degiskenlere mudahale hakki ve dinleme hakki verildi.
    // Buradaki observeAsState metodu sadece Composable fonksiyonlari icinde gecerli.
    // onCreate(savedInstanceState: Bundle?) icinde kullnmak icin yani LiveData olayini orada da yapmak icin baska yollar var.
    var frekans=viewModel.frekans.observeAsState(initial = "")
    var alertMesaji = viewModel.alertMesaji.observeAsState(initial = "")
    var alertMesajiGorunsunMu = viewModel.alertMesajiGorunsunMu.observeAsState(initial=false)


    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "Gitar Tuner",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = frekans.value,
            style = MaterialTheme.typography.headlineSmall)
        if (alertMesajiGorunsunMu.value) {
            AlertDialog(
                onDismissRequest = {alertMesajiGorunsunMu.value },   // Boş alana tıklayınca ne olacagi
                title = {
                    Text("Microfon Durumu")
                },
                text = {
                    Text(alertMesaji.value)
                },
                confirmButton = {
                    TextButton(onClick = { alertMesajiGorunsunMu.value }) {
                        Text("Tamam")
                    }
                }
            )
        }

        }
}



@Preview(showBackground = true)
@Composable
fun FrontEndPreview() {
    MyGuitarTunerTheme {
        FrontEnd("Android")
    }
}