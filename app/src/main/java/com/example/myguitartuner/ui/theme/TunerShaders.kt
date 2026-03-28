package com.example.myguitartuner.ui.theme

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myguitartuner.R

object TunerShaders {

    ////Shaderlar sadece  minSdk = 33 olunca kodlanabiliyor.
    //    //Shadr icin 3 sey lazim. 1.si shader, 2.si infiniteTransition ve 3.su de time


        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @Composable
        fun backgroundWaveShader() {
            //1.Asama: Shader'in yazilmasi
            val shader = remember {
                RuntimeShader(
                    """
        uniform shader image;
        uniform float time;
        uniform float2 resolution;

        half4 main(float2 fragCoord) {
            float2 uv = fragCoord / resolution;

            float waveStrength = 0.02;
            float waveFrequency = 10.0;
            float waveSpeed = 2.0;

            uv.x += sin((uv.y * waveFrequency) + (time * waveSpeed)) * waveStrength;

            return image.eval(uv * resolution);
        }
        """
                             )
            }


            //2.Asama: Tween kisminin ayarlanmasi. Buradaki kodlar kendisi Coroutin isini hallediyor.
            val infiniteTransition = rememberInfiniteTransition()
            val time by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        100_000,
                        easing = LinearEasing
                                     )
                                                  )
                                                       )


            //3.Asama Image metoduna degerlerin yerlestirilmesi ve .setFloatUniform saesinde uniform degiskenlerinin degerlerinin atanmasi.
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Companion.Crop,
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .scale(1.1f)
                    .graphicsLayer {
                        shader.setFloatUniform(
                            "time",
                            time
                                              )//setFloatUniform() metodu Key/Value turunde calisiyor. Key dedigin Shader'a yazdigin kodun icindeki degisken.Hatta basinda da uniform yaziyor, biz de burada oradaki uniformlarin degerlerini atiyoruz.. Yani Unitydeki properties gibi bir durum var degiskenleri buradan biz gonderiyoruz. Shader da oaradan karsiliyor.
                        shader.setFloatUniform("resolution", size.width, size.height)
                        renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image")
                            .asComposeRenderEffect()
                    })


        }

       // @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @Composable
        fun backgroundTransparentShader() {
            //1.Asama: Shader'in yazilmasi
            val shader = remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    RuntimeShader(
                        """
                        uniform shader image;
            uniform float time;
            uniform float2 resolution;
    
            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
    
                // Resmin gerçek rengini al
                half4 imgColor = image.eval(fragCoord);
    
                float wave = sin((uv.y * 80.0) + time) * 0.9 + 0.9;
    
                // Dalga alpha kontrolü
                float alpha = wave * 400;
    
                // Resmin kendi alpha'sını koru, üstüne efekt uygula
                imgColor.a *= alpha;
    
                return imgColor;
            }
            """
                                 )
                } else {
                    TODO("VERSION.SDK_INT < TIRAMISU")
                }
            }


            //2.Asama: Tween kisminin ayarlanmasi. Buradaki kodlar kendisi Coroutin isini hallediyor.
            val infiniteTransition = rememberInfiniteTransition()
            val time by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        1000_000,
                        easing = LinearEasing
                                     )
                                                  )
            )


            //3.Asama Image metoduna degerlerin yerlestirilmesi ve .setFloatUniform saesinde uniform degiskenlerinin degerlerinin atanmasi.
           Image(
               painter = painterResource(R.drawable.background),
               contentDescription = null,
               contentScale = ContentScale.Companion.Crop,
               modifier = Modifier.Companion
                   .fillMaxSize()
                   .scale(1.1f)
                   .graphicsLayer {
                       shader.setFloatUniform(
                           "time",
                           time
                                             )//setFloatUniform() metodu Key/Value turunde calisiyor. Key dedigin Shader'a yazdigin kodun icindeki degisken.Hatta basinda da uniform yaziyor, biz de burada oradaki uniformlarin degerlerini atiyoruz.. Yani Unitydeki properties gibi bir durum var degiskenleri buradan biz gonderiyoruz. Shader da oaradan karsiliyor.
                       shader.setFloatUniform("resolution", size.width, size.height)
                       renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image")
                           .asComposeRenderEffect()
                   })


        }
        @Composable
        fun backgroundImage(){
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Companion.Crop,
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .scale(1.1f)
                 )
        }



    @Composable
    fun background() {
        val sdk = Build.VERSION.SDK_INT

        if (sdk >= 33) {
            // 1. ADIM: Süzgecin en üstü.
            // Eğer cihaz 33, 34, 35... ise BURAYA GİRER ve işi biter.
            // Alttaki hiçbir koda bakmaz bile.
            backgroundTransparentShader()
        }
        else if (sdk >= 26) {
            // 2. ADIM: Eğer kod buraya ulaştıysa, zaten 1. adımı geçememiş demektir.
            // Yani otomatik olarak cihazın 33'ten KÜÇÜK olduğu kesinleşmiştir.
            // Burada sadece "26'dan büyük mü?" diye bakman yeterlidir.
            // Burası doğal olarak "26 ile 32 arası" (26 <= sdk < 33) olur.
            backgroundImage()
        }

    }


}