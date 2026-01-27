package com.example.myguitartuner

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class MyShaders {

    ////Shaderlar sadece  minSdk = 33 olunca kodlanabiliyor.
    //    //Shadr icin 3 sey lazim. 1.si shader, 2.si infiniteTransition ve 3.su de time
    companion object {

        @Composable fun backgroundWaveShader() {
            //1.Asama: Shader'in yazilmasi
            val shader = remember {
                RuntimeShader("""
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
        """)
            }


            //2.Asama: Tween kisminin ayarlanmasi. Buradaki kodlar kendisi Coroutin isini hallediyor.
            val infiniteTransition = rememberInfiniteTransition()
            val time by infiniteTransition.animateFloat(initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(animation = tween(100_000, easing = LinearEasing)))


            //3.Asama Image metoduna degerlerin yerlestirilmesi ve .setFloatUniform saesinde uniform degiskenlerinin degerlerinin atanmasi.
            Image(painter = painterResource(R.drawable.background), contentDescription = null,contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().scale(1.1f).graphicsLayer {
                shader.setFloatUniform("time", time)//setFloatUniform() metodu Key/Value turunde calisiyor. Key dedigin Shader'a yazdigin kodun icindeki degisken.Hatta basinda da uniform yaziyor, biz de burada oradaki uniformlarin degerlerini atiyoruz.. Yani Unitydeki properties gibi bir durum var degiskenleri buradan biz gonderiyoruz. Shader da oaradan karsiliyor.
                shader.setFloatUniform("resolution", size.width, size.height)
                renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image").asComposeRenderEffect()
            })


        }

        @Composable
        fun backgroundTransparentShader(){
            //1.Asama: Shader'in yazilmasi
            val shader = remember {
                RuntimeShader("""
                    uniform shader image;
        uniform float time;
        uniform float2 resolution;

        half4 main(float2 fragCoord) {
            float2 uv = fragCoord / resolution;

            // Resmin gerçek rengini al
            half4 imgColor = image.eval(fragCoord);

            float wave = sin((uv.y * 10.0) + time) * 0.5 + 0.5;

            // Dalga alpha kontrolü
            float alpha = wave * 0.5;

            // Resmin kendi alpha'sını koru, üstüne efekt uygula
            imgColor.a *= alpha;

            return imgColor;
        }
        """)
            }


            //2.Asama: Tween kisminin ayarlanmasi. Buradaki kodlar kendisi Coroutin isini hallediyor.
            val infiniteTransition = rememberInfiniteTransition()
            val time by infiniteTransition.animateFloat(initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(animation = tween(1000_000, easing = LinearEasing)))


            //3.Asama Image metoduna degerlerin yerlestirilmesi ve .setFloatUniform saesinde uniform degiskenlerinin degerlerinin atanmasi.
            Image(painter = painterResource(R.drawable.background), contentDescription = null,contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().scale(1.1f).graphicsLayer {
                shader.setFloatUniform("time", time)//setFloatUniform() metodu Key/Value turunde calisiyor. Key dedigin Shader'a yazdigin kodun icindeki degisken.Hatta basinda da uniform yaziyor, biz de burada oradaki uniformlarin degerlerini atiyoruz.. Yani Unitydeki properties gibi bir durum var degiskenleri buradan biz gonderiyoruz. Shader da oaradan karsiliyor.
                shader.setFloatUniform("resolution", size.width, size.height)
                renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "image").asComposeRenderEffect()
            })


        }


    }


}