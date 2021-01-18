package com.mahi.savingchallenge.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import kotlin.math.roundToInt

/**
 * Clase que gestiona el fondo de pantalla de el juego
 */
class BackgroundImage(bmp: Bitmap) {
    private var backgroundImage: Bitmap = bmp
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels
    private var scale: Float
    private var newWidth: Int
    private var newHeight: Int
    private var scaledBackgroundImage: Bitmap

    init {
        scale = (backgroundImage.height / screenHeight).toFloat()
        newWidth = (backgroundImage.width / scale).roundToInt()
        newHeight = (backgroundImage.height / (scale + 0.5)).roundToInt()
        scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, newWidth, newHeight, true)
    }

    // cargamos la imagen de fondo en la escala y ubicacion deseada para el juego
    fun draw(canvas: Canvas){
        canvas.drawBitmap(scaledBackgroundImage, 0F, 0F, null)
    }
}
