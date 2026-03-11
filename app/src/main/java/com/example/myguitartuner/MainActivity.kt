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
import androidx.compose.runtime.livedata.observeAsState

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.graphics.Color


//import androidx.compose.ui.graphics.RenderEffect//Bu yanlis olan
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.MutableLiveData


import com.example.myguitartuner.ui.theme.MyGuitarTunerTheme
import com.example.myguitartuner.ui.theme.kremRengi
import com.example.myguitartuner.ui.theme.siyahsi
import kotlinx.coroutines.delay


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
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
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
                viewModel.updateAlertMesaji("mic zaten izin verilmis")
                // viewModel.startTuning()
                viewModel.startTuningInViewModel()


            }

            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                viewModel.updateAlertMesaji("mic izin verilmemis")
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)//ilk istege onay verilmediyse ikinci baslatmada da bi yeniistek iyi olur.
            }

            else -> {
                viewModel.updateAlertMesaji("mic ilk defa izin istenmis")
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)//Izin isteme ekranini  aciyor ve sonucu da kaydediyor.(While using the app) ekranini aciyor yani
            }
        }
    }

}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FrontEnd(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val pitch = viewModel.pitch.observeAsState(0)
    val yuzdesi = viewModel.yuzdesi.observeAsState(0)
    val renk = viewModel.renk.observeAsState()//0 kirmizi, 1 yesil
    val activeString = viewModel.activeString.observeAsState()//0 kirmizi, 1 yesil
    val selectedBirinci=viewModel.selectedBirinci.observeAsState()
    val selectedIkinci=viewModel.selectedIkinci.observeAsState()
    val selectedUcuncu=viewModel.selectedUcuncu.observeAsState()
    val selectedDorduncu=viewModel.selectedDorduncu.observeAsState()
    val selectedBesinci=viewModel.selectedBesinci.observeAsState()
    val selectedAltinci=viewModel.selectedAltinci.observeAsState()
    val string1=viewModel.string1.observeAsState(emptyList<MyNoteDataClass>())
    val string2=viewModel.string2.observeAsState(emptyList<MyNoteDataClass>())
    val string3=viewModel.string3.observeAsState(emptyList<MyNoteDataClass>())
    val string4=viewModel.string4.observeAsState(emptyList<MyNoteDataClass>())
    val string5=viewModel.string5.observeAsState(emptyList<MyNoteDataClass>())
    val string6=viewModel.string6.observeAsState(emptyList<MyNoteDataClass>())



    val density = LocalDensity.current
    val widthInDp = with(density) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    val popupAcikMi = remember { mutableStateOf(false) }



    //Burayi LaunchedEffect(pitch.value) seklinde deneyecegim
    LaunchedEffect(Unit) {
          while (true) {
            viewModel.updateYuzdeVeRenk(82, pitch.value); delay(10)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MyShaders.background()
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
                            .size(widthInDp * 0.8f)
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


                            text = yuzdesi.value.toString(), style = TextStyle(
                                color = renk.value ?: kremRengi,
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally), text = "%",

                            style = TextStyle(
                                color = renk.value ?: kremRengi,
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
                        painter = painterResource(selectedBirinci.value?.icon ?: R.drawable.e_),
                        contentDescription = "E 1.tel",
                        modifier = Modifier
                            // .border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.015f, -0.98f))
                            .clickable {
                                if (popupAcikMi.value == false) {
                                    viewModel.updateActiveString(string1.value)
                                    popupAcikMi.value = true
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        //  alpha = if (expandedBirinci.value) 1f else 0f

                    )
                    Image(
                        painter = painterResource(selectedIkinci.value?.icon ?: R.drawable.b_),
                        contentDescription = "B 2.tel",
                        modifier = Modifier
                            //.border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.4f, -0.98f))
                            .clickable {
                                if (popupAcikMi.value == false) {
                                    viewModel.updateActiveString(string2.value)
                                    popupAcikMi.value = true
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        //alpha = if (expandedIkinci.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(selectedUcuncu.value?.icon ?: R.drawable.g_),
                        contentDescription = "G 3.tel",
                        modifier = Modifier
                            // .border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.78f, -1f))

                            .clickable {
                                if (popupAcikMi.value == false) {
                                    viewModel.updateActiveString(string3.value)
                                    popupAcikMi.value = true
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        // alpha = if (expandedUcuncu.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(selectedDorduncu.value?.icon ?: R.drawable.d_),
                        contentDescription = "D 4.tel",
                        modifier = Modifier
                            // .border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.78f, 1f))
                            //.size(35.dp, 55.dp)
                            .clickable {
                                if (popupAcikMi.value == false) {
                                    viewModel.updateActiveString(string4.value)
                                    popupAcikMi.value = true
                                }
                            },
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        // alpha = if (expandedDorduncu.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(selectedBesinci.value?.icon ?: R.drawable.a_),
                        contentDescription = "A 5.tel",
                        modifier = Modifier
                            //.border(2.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.4f, 0.96f))
                            .clickable {
                                if (popupAcikMi.value == false) {
                                    viewModel.updateActiveString(string5.value)
                                    popupAcikMi.value = true
                                }
                            }, // Tıklama ile görünürlük değiştir
                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        //alpha = if (expandedBesinci.value) 1f else 0f
                    )
                    Image(
                        painter = painterResource(selectedAltinci.value?.icon ?: R.drawable.e_),
                        contentDescription = "E 6.tel",
                        modifier = Modifier
                            // .border(7.dp, Color.Blue)
                            .fillMaxSize(0.15f)//Boyutu da bununla ayarla .size ile farkina bak
                            .align(BiasAlignment(-0.015f, 0.98f))
                            //.size(35.dp, 55.dp)
                            .clickable {
                                if (popupAcikMi.value == false) {
                                    viewModel.updateActiveString(string6.value)
                                    popupAcikMi.value = true
                                }
                            },

                        contentScale = ContentScale.Fit, // Görseli boyutlandırarak yerleştirme
                        // alpha = if (expandedAltinci.value) 1f else 0f
                    )

                }
            }
            Text(
                text = pitch.value.toString(), style = TextStyle(
                    color = renk.value ?: kremRengi, fontSize = 72.sp, fontWeight = FontWeight.Bold
                )
            )


        }


        if (popupAcikMi.value) {
            Popup(
                // Ekranın tam ortasına hizalar
                alignment = Alignment.Center,
                onDismissRequest = { popupAcikMi.value = false },
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

                                activeString.value?.forEach { item ->
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
                                                    text = item.frekans,
                                                    color = kremRengi,
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier.background(if (item.highlighting) Color.Black else siyahsi.copy(alpha = 0.3f)),
                                        onClick = {
                                            item.onSelected(item)
                                            viewModel.saveNotesinViewModelTryCatch(item)
                                            viewModel.updateHighlighting(item)

                                            popupAcikMi.value = false
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

