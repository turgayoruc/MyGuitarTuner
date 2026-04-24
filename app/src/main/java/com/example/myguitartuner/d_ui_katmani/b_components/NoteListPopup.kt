package com.example.myguitartuner.d_ui_katmani.b_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.myguitartuner.d_ui_katmani.a_theme.siyahsi
import com.example.myguitartuner.d_ui_katmani.d_intents.TunerIntent
import com.example.myguitartuner.d_ui_katmani.c_states.TunerUiState
import com.example.myguitartuner.d_ui_katmani.e_viewModels.TunerViewModel

@Composable
fun NoteListPopup(state: TunerUiState, viewModel: TunerViewModel){
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
                                            text = item.frekansString.toString(),
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