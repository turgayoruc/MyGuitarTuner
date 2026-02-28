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
                viewModel.startTuning()


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

    val buffer =
        viewModel.buffer.observeAsState("")//Jetcompose'un en onemli aparati. viewModelde MutableLiveData ile olusturdugun degiskenleri burada dinleyebiliyoraun.
    val alertMesaji =
        viewModel.alertMesaji.observeAsState("")//Bunu kullanmazsan degisimleri takip edemezsin sadece mesela Text kendini yeilerse yenisini okuyabilirsin. Ama bunu kullnirsan buradaki degisime bagli degiskeni kimler dinliyorsa onlar otomatik kendileri yeniden calisir ve gucellmis olur.
    val pitch = viewModel.pitch.observeAsState(0)
    val yuzdesi = viewModel.yuzdesi.observeAsState(0)
    val renk = viewModel.renk.observeAsState()//0 kirmizi, 1 yesil

    val density = LocalDensity.current
    val widthInDp = with(density) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    val popupAcikMi = remember { mutableStateOf(false) }
    //val aktifTelListesi = remember { mutableStateOf(emptyList<MyNoteDataClass>()) }
    val enumString=remember { mutableStateOf(MyEnumString.SITRING1) }

    val selectedBirinci = remember { mutableStateOf<MyNoteDataClass?>(null) }
    val selectedIkinci = remember { mutableStateOf<MyNoteDataClass?>(null) }
    val selectedUcuncu = remember { mutableStateOf<MyNoteDataClass?>(null) }
    val selectedDorduncu = remember { mutableStateOf<MyNoteDataClass?>(null) }
    val selectedBesinci = remember { mutableStateOf<MyNoteDataClass?>(null) }
    val selectedAltinci = remember { mutableStateOf<MyNoteDataClass?>(null) }

    //val suankiSelectedTel = remember { mutableStateOf<MutableState<MyNoteItem?>>(selectedBirinci) }


    val string1 = listOf(
        MyNoteDataClass(false,R.drawable.c_, "C4-261.63 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(false,R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(false,R.drawable.d_, "D4-293.66 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(false,R.drawable.ddiyez_, "D♯4 / E♭4-311.13 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(true,R.drawable.e_, "E4-329.63 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(false,R.drawable.f_, "F4-349.23 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(false,R.drawable.f_, "F♯4 / G♭4-369.99 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(false,R.drawable.g_, "G4-392.00 Hz", { selectedBirinci.value = it })
    )
    val string2 = listOf(
        MyNoteDataClass(false,R.drawable.g_, "G3-196.00 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(false,R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(false,R.drawable.a_, "A3-220.00 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(false,R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(true,R.drawable.b_, "B3-246.94 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(false,R.drawable.c_, "C4-261.63 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(false,R.drawable.cdiyez_, "C♯4 / D♭4-277.18 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(false,R.drawable.d_, "D4-293.66 Hz", { selectedIkinci.value = it })
    )
    val string3 = listOf(
        MyNoteDataClass(false,R.drawable.ddiyez_, "D♯3 / E♭3-155.56 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(false,R.drawable.e_, "E3-164.81 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(false,R.drawable.f_, "F3-174.61 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(false,R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(true,R.drawable.g_, "G3-196.00 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(false,R.drawable.gdiyez_, "G♯3 / A♭3-207.65 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(false,R.drawable.a_, "A3-220.00 Hz") { selectedUcuncu.value = it },
        MyNoteDataClass(false,R.drawable.adiyez_, "A♯3 / B♭3-233.08 Hz") { selectedUcuncu.value = it }
    )
    val string4 = listOf(
        MyNoteDataClass(false,R.drawable.adiyez_, "B♭2-116.54 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(false,R.drawable.b_, "B2-123.47 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(false,R.drawable.c_, "C3-130.81 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(false,R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(true, R.drawable.d_, "D3-146.83 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(false,R.drawable.e_, "E3-164.81 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(false,R.drawable.f_, "F3-174.61 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(false,R.drawable.fdiyez_, "F♯3 / G♭3-185.00 Hz", { selectedDorduncu.value = it })
    )
    val string5 = listOf(
        MyNoteDataClass(false,R.drawable.f_, "F2-87.31 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(false,R.drawable.fdiyez_, "F♯2 / G♭2-92.50 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(false,R.drawable.g_, "G2-98.00 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(false,R.drawable.gdiyez_, "G♯2 / A♭2-103.83 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(true,R.drawable.a_, "A2-110.00 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(false,R.drawable.b_, "B2-123.47 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(false,R.drawable.c_, "C3-130.81 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(false,R.drawable.cdiyez_, "C♯3 / D♭3-138.59 Hz", { selectedBesinci.value = it })
    )
    val string6 = listOf(
        MyNoteDataClass(false,R.drawable.c_, "C2-65.41 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(false,R.drawable.cdiyez_, "C♯2 / D♭2-69.30 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(false,R.drawable.d_, "D2-73.42 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(false,R.drawable.ddiyez_, "D♯2 / E♭2-77.78 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(true, R.drawable.e_, "E2-82.41 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(false,R.drawable.f_, "F2-92.50 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(false,R.drawable.fdiyez_, "F♯2 / G♭2-98.00 Hz", { selectedAltinci.value = it }),
        MyNoteDataClass(false,R.drawable.g_, "G2-103.83 Hz", { selectedAltinci.value = it })
    )
    val stringDefault = listOf(
        MyNoteDataClass(true,R.drawable.e_, "E4-329.63 Hz", { selectedBirinci.value = it }),
        MyNoteDataClass(true,R.drawable.b_, "B3-246.94 Hz", { selectedIkinci.value = it }),
        MyNoteDataClass(true,R.drawable.g_, "G3-196.00 Hz", { selectedUcuncu.value = it }),
        MyNoteDataClass(true,R.drawable.d_, "D3-146.83 Hz", { selectedDorduncu.value = it }),
        MyNoteDataClass(true,R.drawable.a_, "A2-110.00 Hz", { selectedBesinci.value = it }),
        MyNoteDataClass(true,R.drawable.e_, "E2-82.41 Hz", { selectedAltinci.value = it })
    )


  //  val seciliPopupinRengi = remember { mutableStateOf<Color>(siyahsi.copy(alpha = 0.8f)) }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.updateYuzdeVeRenk(82, pitch.value); delay(10)
        }
    }

    //En alttaki levele yani 1.levele tum ekrani kapsatan box. Tabana bir image koymak icin mecburdum
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MyShaders.background()//Bunun en altta taban olmasini istedigim icin bundan sonra bir Column ya da Bir Box eklemem sart. Yani altina gelmemsi icine bir sonraki eklyecegim sey bunun ustunu ortebilecek bir seye ihtiyacim var. Box ust uste binecegi icin ortebilir.
        //2.levele alt alta iki box ekemek icin Column lazim oldu.Eger BOx'larin height'larini ya da .weight'lerini ayarlamazsan ust uste binerler alttaki ezilir.
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            //3.levele frekansi gosterecek kisim
            Box(
                modifier = Modifier.weight(50f)
                // .border(2.dp, Color.Red)
            ) {
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



            Box(
                modifier = Modifier
                    // .border(2.dp, color = Color.Green)
                    .weight(50f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .aspectRatio(417f / 220f)//Bunun sayesinde ust uste resim ekleme guzel calisyior
                    // .border(4.dp, color = Color.Yellow)
                ) {
                    Image(
                        painter = painterResource(R.drawable.gitar),
                        contentDescription = "Gitar",
                        modifier = Modifier
                            // .border(5.dp, Color.Blue)
                            .fillMaxSize()//.aspectRatio(417f/220f)'ya verdigim deger resim ile ayni oldugu icin tam oturacak. Bu .fill yapilari contentin cercevesinibelirtiyor.
                        ,
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
                                   // suankiSelectedTel.value=selectedBirinci//DropdownItemMenusu siralandiginda onceden secili olan notanin rengini farkli gostermek icin


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
                                   // suankiSelectedTel.value=selectedIkinci


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
                                   // suankiSelectedTel.value=selectedUcuncu

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
                                    //suankiSelectedTel.value=selectedDorduncu


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
                                    //suankiSelectedTel.value=selectedBesinci

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
                                    //suankiSelectedTel.value=selectedAltinci


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
//        Box(modifier = Modifier.align(Alignment.Center)) {
//
//        }

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
                            if (aktifTelListesi.value.isNotEmpty()) {
                                aktifTelListesi.value.forEach { item ->
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
                                                    text = item.label,
                                                    color = kremRengi,
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier.background(if (item.isSelected) siyahsi.copy(alpha = 1f) else siyahsi.copy(alpha = 0.3f)),
                                        onClick = {
                                            item.onSelected(item)
                                            MyEnumString.updateIsSelected(enumString,item,string1,string2,string3,string4,string5,string6)
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

//        DropdownMenu(
//            // modifier = Modifier.size(300.dp)
//
//            expanded = dropdownAcikMi.value,
//            onDismissRequest = { dropdownAcikMi.value = false },
//             offset = DpOffset(x = 0.dp, y = 0.dp)
//        ) {
//
//            if (hangiTelinNotalarininlistesi.value.isNotEmpty()){
//                hangiTelinNotalarininlistesi.value.forEach { item ->
//                    val isSelected = hangiTelinNotalarininlistesi.value == item
//
//                    DropdownMenuItem(text = {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            // Add image here (replace "your_image" with actual resource)
//                            Image(
//                                painter = painterResource(id = item.icon), // Replace with your image
//                                contentDescription = "Icon", modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            val animatedColor by animateColorAsState(
//                                targetValue = if (isSelected) Color.Yellow else Color.Black
//                            )
//                            Text(
//                                text = item.label,
//                                color = if (isSelected) Color.Yellow else Color.Black, // Text color change on selection
//                                style = TextStyle(
//                                    fontWeight = FontWeight.Bold, fontSize = 16.sp
//                                )
//                            )
//                        }
//                    }, onClick = {
//                        item.onSelected(item)
//                        dropdownAcikMi.value= false
//
//                    })
//                }}
//        }
    }


}



//@Preview(showBackground = true)
//@Composable
//fun FrontEndPreview() {
//    MyGuitarTunerTheme {
//        FrontEnd(name = "Preview Name", modifier = Modifier)
//    }
//}