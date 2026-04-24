package com.example.myguitartuner.d_ui_katmani.b_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myguitartuner.R
import com.example.myguitartuner.d_ui_katmani.d_intents.TunerIntent
import com.example.myguitartuner.d_ui_katmani.c_states.TunerUiState
import com.example.myguitartuner.d_ui_katmani.e_viewModels.TunerViewModel


@Composable
fun ColumnScope.AltComponent(state: TunerUiState, viewModel: TunerViewModel){
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
}