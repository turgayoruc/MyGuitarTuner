package com.example.myguitartuner.d_ui_katmani.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myguitartuner.d_ui_katmani.b_components.AltComponent
import com.example.myguitartuner.d_ui_katmani.b_components.NoteListPopup
import com.example.myguitartuner.d_ui_katmani.b_components.UstComponent
import com.example.myguitartuner.d_ui_katmani.d_intents.TunerIntent
import com.example.myguitartuner.d_ui_katmani.shaders.TunerShaders
import com.example.myguitartuner.d_ui_katmani.e_viewModels.TunerViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier, viewModel: TunerViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val widthInDp = with(density) { windowInfo.containerSize.width.toDp() }
    LaunchedEffect(widthInDp) { viewModel.onIntent(TunerIntent.UpdateScreenWidth(widthInDp.value)) }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TunerShaders.background()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
              ) {
            UstComponent(state)
            AltComponent(state,viewModel)
            Text(text = state.pitch.toString(), style = TextStyle(color = state.renk , fontSize = 72.sp, fontWeight = FontWeight.Bold )
                )
        }
        NoteListPopup(state,viewModel)





    }


}