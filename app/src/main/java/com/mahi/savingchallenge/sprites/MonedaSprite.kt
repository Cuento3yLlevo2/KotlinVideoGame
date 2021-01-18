package com.mahi.savingchallenge.sprites

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaPlayer
import com.mahi.savingchallenge.R
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

/**
 * Clase que gestiona todas las monedas
 */
class MonedaSprite(bmp: Bitmap, var monedaType: Int, context: Context) {
    // delay for bag of coins
    private var timeForBagofCoins = true
    // Sonido moneda interceptada
    private var coinIntersectedSound : MediaPlayer = MediaPlayer()
    private var gameViewContext = context
    // cargamos imagen de moneda
    private var monedaImage: Bitmap = bmp
    // cargamos coordenadas
    private var monedaX : Float
    private var monedaY : Float
    var monedaRect : Rect
    private var leftHitboxArrangement: Int = 0
    private var topHitboxArrangement: Int = 0
    private var rightHitboxArrangement: Int = 0
    private var bottomHitboxArrangement: Int = 0
    private var monedaSpeed: Int = 0
    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        // cargamos coordenadas random para ubicar la moneda arriba de la pantalla
        monedaX = Random.nextInt(40, screenWidth - 80).toFloat()
        monedaY = Random.nextInt(-1300, - 70).toFloat()
        /* dependiendo de el tipo de moneda:
            - ubicamos el hitbox correctamente
            - cargamos el sonido por tipo de moneda
            - cargamos la velocidad por tipo de moneda
        */
        when (monedaType) {
            COIN -> {
                leftHitboxArrangement = 6
                topHitboxArrangement = 5
                rightHitboxArrangement = 6
                bottomHitboxArrangement = 5
                monedaSpeed = 10
                coinIntersectedSound = MediaPlayer.create(gameViewContext, R.raw.coin_intersected_sound)
                coinIntersectedSound = MediaPlayer.create(gameViewContext, R.raw.coin_intersected_sound)
                coinIntersectedSound.setVolume(0.3F, 0.3F)
            }
            BAG -> {
                leftHitboxArrangement = 28
                topHitboxArrangement = 48
                rightHitboxArrangement = 28
                bottomHitboxArrangement = 7
                monedaSpeed = 26
                coinIntersectedSound = MediaPlayer.create(gameViewContext, R.raw.coin_intersected_sound)
                coinIntersectedSound = MediaPlayer.create(gameViewContext, R.raw.oink_pig)
                coinIntersectedSound.setVolume(0.4F, 0.4F)
            }
        }
        // monedaRect = hitbox de las monedas
        monedaRect = Rect(monedaX.toInt()+leftHitboxArrangement, monedaY.toInt()+topHitboxArrangement, monedaX.toInt()+monedaImage.width-rightHitboxArrangement, monedaY.toInt()+monedaImage.height-bottomHitboxArrangement)
    }
    // pintamos la moneda
    fun draw(canvas: Canvas){
        canvas.drawBitmap(monedaImage, monedaX, monedaY, null)
        // *canvas.drawRect pintar el hitbox solo para pruebas
        // canvas.drawRect(monedaRect, Paint().apply { color = Color.BLUE })
    }

    /**
     * cada vez que se repinta la moneda se actualiza su ubicacion
     */
    fun update() {
        if (monedaType == BAG){
            if (timeForBagofCoins){
                if (monedaY < screenHeight) {
                    moveDownCoin()
                } else {
                    resetCoin()
                }
            }
        } else {
            if (monedaY < screenHeight) {
                moveDownCoin()
            } else {
                resetCoin()
            }
        }
    }

    /**
     * reinicia la moneda si hay colisión con el cerdito
     * reproduce el sonido de colisión
     */
    fun resetIfIntersected(intersectedCoin: Rect?) {
        if (intersectedCoin != null) {
            if (intersectedCoin.intersect(this.monedaRect)){
                coinIntersectedSound.start()
                resetCoin()
            }
        }
    }

    /**
     * repinta la moneda en pantalla hacia abajo dependiendo de la velocidad del tipo de moneda
     */
    private fun moveDownCoin() {
        monedaY += monedaSpeed
        monedaRect.top = monedaY.toInt()+topHitboxArrangement
        monedaRect.bottom = monedaY.toInt()+monedaImage.height-bottomHitboxArrangement
    }

    /**
     * Reinicia las coordenadas de la moneda
     * si la moneda es de tipo BAG = bolsa de monedas inicia un contador interno de 8 segundos para la bolsa de monedas
     */
    private fun resetCoin() {
        monedaY = Random.nextInt(-1300, - 70).toFloat()
        monedaX = Random.nextInt(40, screenWidth - 80).toFloat()
        monedaRect.set(monedaX.toInt()+leftHitboxArrangement, monedaY.toInt()+topHitboxArrangement, monedaX.toInt()+monedaImage.width-rightHitboxArrangement, monedaY.toInt()+monedaImage.height-bottomHitboxArrangement)
        if (monedaType == BAG){
            timeForBagofCoins = false
            Timer("SettingUp", false).schedule(8000L) { timeForBagofCoins = true }
        }
    }
    /**
     * Cada constante representa un tipo de moneda
     */
    companion object {
        // tipo de monedas
        const val COIN: Int = 1
        const val BAG: Int = 2
    }
}