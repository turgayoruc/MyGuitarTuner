package com.example.myguitartuner


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts//Microfon Permission islemlerini disaridaki bir class ile yapamadim.Ilerde bakarsin
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.graphics.Color


//import androidx.compose.ui.graphics.RenderEffect//Bu yanlis olan
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle


import com.example.myguitartuner.ui.theme.MyGuitarTunerTheme
import com.example.myguitartuner.ui.theme.TunerShaders
import com.example.myguitartuner.ui.theme.siyahsi
import com.example.myguitartuner.ui.tuner.TunerIntent
import com.example.myguitartuner.ui.tuner.TunerViewModel


import kotlin.getValue

class MainActivity : ComponentActivity() {

    val viewModel: TunerViewModel by viewModels()
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FrontEnd(name: String, modifier: Modifier = Modifier, viewModel: TunerViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val widthInDp = with(density) { windowInfo.containerSize.width.toDp() }
    LaunchedEffect(widthInDp) { viewModel.onIntent(TunerIntent.UpdateScreenWidth(widthInDp.value)) }

  //  val popupAcikMi = remember { mutableStateOf(false) }



//    //Burayi LaunchedEffect(pitch.value) seklinde deneyecegim
//    LaunchedEffect(Unit) {
//          while (true) {
//            //viewModel.updateYuzdeVeRenk(82, pitch.value); delay(10)
//              viewModel.onIntent(Intent.UpdateYuzde(82))
//              delay(100)
//              //viewModel.onIntent(Intent.UpdateRenk())
//        }
//    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TunerShaders.background()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box( modifier = Modifier.weight(50f))
            {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.frekans),
                        contentDescription = null,
                        modifier = Modifier
                            .size(state.screenWidthDp.dp * 0.8f)
                            .align(alignment = Alignment.Center)
                        // .border(2.dp, Color.Red)
                        ,
                        contentScale = ContentScale.Fit
                    )
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),


                            text = state.yuzde.toString(), style = TextStyle(
                                color = state.renk,
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally), text = "%",

                            style = TextStyle(
                                color = state.renk,
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                }

            }

            Box(modifier = Modifier.weight(50f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .aspectRatio(417f / 220f)//Bunun sayesinde ust uste resim ekleme guzel calisyior
                ) {
                    Image(
                        painter = painterResource(R.drawable.gitar),
                        contentDescription = "Gitar",
                        modifier = Modifier.fillMaxSize() ,//.aspectRatio(417f/220f)'ya verdigim deger resim ile ayni oldugu icin tam oturacak. Bu .fill yapilari contentin cercevesinibelirtiyor.
                        contentScale = ContentScale.FillBounds//Ust uste resim koyacaksan alttaki resimde olmasi sart yoksa baska telefona gectiginde ustteki resimlerde kayma olur..Fit olursa resmin en boy orani korunur
                    )
                    Image(
                        painter = painterResource(state.selectedBirinci.icon ),
                        contentDescription = "E 1.tel",
                        modifier = Modifier
                            // .border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.015f, -0.98f))
                            .clickable {
                                if (!state.popupAcikMi) {
                                    //viewModel.updateActiveString(string1.value)
                                    viewModel.onIntent(TunerIntent.UpdateActiveString(state.string1))
                                    viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(true))
                                    //popupAcikMi.value = true
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        //  alpha = if (expandedBirinci.value) 1f else 0f

                    )
                    Image(
                        painter = painterResource(state.selectedIkinci.icon ?: R.drawable.b_),
                        contentDescription = "B 2.tel",
                        modifier = Modifier
                            //.border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.4f, -0.98f))
                            .clickable {
                                if (!state.popupAcikMi) {
                                   // viewModel.updateActiveString(string2.value)
                                    viewModel.onIntent(TunerIntent.UpdateActiveString(state.string2))
                                    viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(true))
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        //alpha = if (expandedIkinci.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(state.selectedUcuncu.icon ?: R.drawable.g_),
                        contentDescription = "G 3.tel",
                        modifier = Modifier
                            // .border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.78f, -1f))

                            .clickable {
                                if (!state.popupAcikMi) {
                                    //viewModel.updateActiveString(string3.value)
                                    viewModel.onIntent(TunerIntent.UpdateActiveString(state.string3))
                                    viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(true))
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        // alpha = if (expandedUcuncu.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(state.selectedDorduncu.icon ),
                        contentDescription = "D 4.tel",
                        modifier = Modifier
                            // .border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.78f, 1f))
                            //.size(35.dp, 55.dp)
                            .clickable {
                                if (!state.popupAcikMi) {
                                   // viewModel.updateActiveString(string4.value)
                                    viewModel.onIntent(TunerIntent.UpdateActiveString(state.string4))
                                    viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(true))
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        // alpha = if (expandedDorduncu.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(state.selectedBesinci.icon ),
                        contentDescription = "A 5.tel",
                        modifier = Modifier
                            //.border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.4f, 0.96f))
                            .clickable {
                                if (!state.popupAcikMi) {
                                   // viewModel.updateActiveString(string5.value)
                                    viewModel.onIntent(TunerIntent.UpdateActiveString(state.string5))
                                    viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(true))
                                }
                            }, // Tıklama ile görünürlük değiştir
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        //alpha = if (expandedBesinci.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(state.selectedAltinci.icon),
                        contentDescription = "E 6.tel",
                        modifier = Modifier
                            // .border(7.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.015f, 0.98f))
                            //.size(35.dp, 55.dp)
                            .clickable {
                                if (!state.popupAcikMi) {
                                    //viewModel.updateActiveString(string6.value)
                                    viewModel.onIntent(TunerIntent.UpdateActiveString(state.string6))
                                    viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(true))
                                }
                            },

                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        // alpha = if (expandedAltinci.value) 1f else 0f
                    )

                }
            }
            Text(
                text = state.pitch.toString(), style = TextStyle(
                    color = state.renk , fontSize = 72.sp, fontWeight = FontWeight.Bold
                )
            )


        }


        if (state.popupAcikMi) {
            Popup(
                // Ekranın tam ortasına hizalar
                alignment = Alignment.Center,
                onDismissRequest = { viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(false)) },
                properties = PopupProperties(
                    focusable = true, // Dışarı tıklandığında kapanması ve geri tuşu için şart
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                )
            ) {
                // DropdownMenu'nün görsel çerçevesini simüle ediyoruz
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = siyahsi.copy(alpha = 0.3f) ), // Arka plan rengi
                    modifier = Modifier
                        .width(280.dp) // Sabit genişlik veya wrapContentWidth()
                        .padding(16.dp)
                ) {

                    Box(modifier = Modifier.fillMaxWidth()) {

                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                // 8 item'ın hepsi sığmazsa diye scroll ekliyoruz
                                .verticalScroll(rememberScrollState())
                        ) {

                                state.activeString.forEach { item ->
                                    // Not: isSelected kontrolünde 'item' ile listenin kendisini kıyaslamışsın.
                                    // Muhtemelen seçili olanı tutan başka bir state ile kıyaslamalısın.
                                   // val isSelected =  suankiSelectedTel.value.value == item // Burayı kendi seçili state'inle güncelle


                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(id = item.icon),
                                                    contentDescription = "Icon",
                                                    modifier = Modifier.size(40.dp)
                                                )
                                                Spacer(modifier = Modifier.width(25.dp))

                                                //                                            val animatedColor by animateColorAsState(
                                                //                                                targetValue = if (isSelected) Color.Black else kremRengi
                                                //                                            )

                                                Text(
                                                    text = item.frekans.toString(),
                                                    color = state.kremRengi,
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier.background(if (item.highlighting) Color.Black else siyahsi.copy(alpha = 0.3f)),
                                        onClick = {
//                                            item.onSelected(item)
//                                            viewModel.saveNotesinViewModelTryCatch(item)
//                                            viewModel.updateHighlighting(item)
                                            viewModel.onIntent(TunerIntent.UpdateHighlighting(item))
                                            viewModel.onIntent(TunerIntent.UpdateSelectedAllString(item))

                                            viewModel.onIntent(TunerIntent.UpdatePopupAcikMi(false))
                                        }

                                    )

                            }
                        }
                    }
                }
            }
        }


    }


}

