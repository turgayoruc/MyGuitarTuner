package com.example.myguitartuner.d_ui_katmani.b_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myguitartuner.R
import com.example.myguitartuner.d_ui_katmani.c_states.TunerUiState

@Composable
fun ColumnScope.UstComponent( state: TunerUiState){
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
}