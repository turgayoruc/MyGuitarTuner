package com.example.myguitartuner


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



import kotlin.compareTo
import kotlin.run

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.myguitartuner.ui.theme.MyGuitarTunerTheme


class MainViewModel: ViewModel() {
    var frekans= MutableLiveData<String>()
    var alertMesaji= MutableLiveData<String>()
    var alertMesajiGorunsunMu= MutableLiveData<Boolean>()



    init {
         frekans= MutableLiveData<String>("0")
         alertMesaji= MutableLiveData<String>("")
         alertMesajiGorunsunMu= MutableLiveData<Boolean>(false)
    }




}