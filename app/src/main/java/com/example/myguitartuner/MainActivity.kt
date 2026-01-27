package com.example.myguitartuner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.RuntimeShader
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts//Microfon Permission islemlerini disaridaki bir class ile yapamadim.Ilerde bakarsin
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.RenderEffect//Bu dogru olan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
//import androidx.compose.ui.graphics.RenderEffect//Bu yanlis olan
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


import com.example.myguitartuner.ui.theme.MyGuitarTunerTheme


import kotlin.getValue

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("toruc", "mic oncesi")
        checkMicrophonePermission()
        setContent {
            MyGuitarTunerTheme {
                Scaffold() { innerPadding ->
                    FrontEnd(name = "Android", modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }

    }


    //isGranted eger kullanici microfona izin verdiyse true oluyor eger izin vermezse de false oluyor.
    //ilk onay tiklamasinda yapilmasini istediklerini buraya, program tekrar acildiginda mic izni tekrar iztemeyecekse de yapilmasi gerekenleri ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO ) == PackageManager.PERMISSION_GRANTED -> {} icine yazmalisin
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // viewModel.startTuning()

            // viewModel.updateAlertMesaji("izni onayladiniz")
            //   checkMicrophonePermission()//Bu tekrar check islemii aliskanlik haline getir. Cunku izne bagli metolar check islemine gore calisabiliyor sanki. Check yapmadan direkt baslatirsan uygulama cokuyor.
            // viewModel.startTuning()//Birda olmaz cunku bu metoda gidersen izne tabioldugunu gorursun.O yuzden burada tekrar check islemini yapiyorum.
        }
        else {
            // Kullanıcı izni reddetti
        }
    }

    //Burasi sadece izinlerin kaydini kontrol ediyor, kayit durumuna gore istedigin metodu baslatirsin. Izin isteme ekranini requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO) aciyor ve sonucu da kaydediyor.
    private fun checkMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.updateAlertMesaji("mic zaten izin verilmis")
                // viewModel.startTuning()
                viewModel.startTuning()


            }

            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)                                         -> {
                viewModel.updateAlertMesaji("mic izin verilmemis")
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)//ilk istege onay verilmediyse ikinci baslatmada da bi yeniistek iyi olur.
            }

            else                                                                                                           -> {
                viewModel.updateAlertMesaji("mic ilk defa izin istenmis")
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)//Izin isteme ekranini  aciyor ve sonucu da kaydediyor.(While using the app) ekranini aciyor yani
            }
        }
    }

}

@Composable fun FrontEnd(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // val engineViewModel: MyAudioEngine = viewModel() bu composable icin. Bu da activity icin val viewModel: MainViewModel by viewModels()
    // val buttonDegeri = remember { mutableStateOf("Başlangıç Metni") } sadece burada lusturup kullnacagin degiskenler icin.

    val buffer = viewModel.buffer.observeAsState("")//Jetcompose'un en onemli aparati. viewModelde MutableLiveData ile olusturdugun degiskenleri burada dinleyebiliyoraun.
    val alertMesaji = viewModel.alertMesaji.observeAsState("")//Bunu kullanmazsan degisimleri takip edemezsin sadece mesela Text kendini yeilerse yenisini okuyabilirsin. Ama bunu kullnirsan buradaki degisime bagli degiskeni kimler dinliyorsa onlar otomatik kendileri yeniden calisir ve gucellmis olur.
    val pitch = viewModel.pitch.observeAsState(0)


    Box(modifier = Modifier.fillMaxSize()) {
       // Image(painter = painterResource(R.drawable.background), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        //MyShaders.backgroundWaveShader()
       // MyShaders.backgroundTransparentShader()
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
            //MyShaderAnimation(shader = shader, time = timeAnimator, R.drawable.rose)


//        Text(text = pitch.value.toString(), style = MaterialTheme.typography.headlineSmall)
//        Spacer(modifier = Modifier.height(32.dp))

            //  Button({buttonDegeri.value="01234"}) { Text( text = "MyButton",style = MaterialTheme.typography.headlineSmall ) }
            //  Spacer(modifier = Modifier.height(32.dp))

            //  Text(text = alertMesaji.value.toString(), style = MaterialTheme.typography.headlineSmall)

// Text(text = "Gitar Tuner", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(32.dp))

        }
    }


}





//
//@Preview(showBackground = true)
//@Composable
//fun FrontEndPreview() {
//    MyGuitarTunerTheme {
//        FrontEnd("Android",)
//    }
//}