package com.mahi.savingchallenge.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

/**
 * Clase que gestiona el Cerdido
 */
class CerditoSprite(cerditoBmp: Bitmap, ufoBmp: Bitmap) {
    // cagamos la imagen de el cerdito y su nave
    private var cerditoImage: Bitmap = cerditoBmp
    private var ufoImage: Bitmap = ufoBmp
    // Pre cargamos coordenadas
    private var cerditoX : Float
    private var cerditoY : Float
    private var ufoX : Float
    private var ufoY : Float
    var cerditoRect : Rect
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels

    // Ubicamos a el cerdito y su nave en pantalla
    init {
        cerditoX = 200F
        cerditoY = screenHeight.toFloat() - 335F
        ufoX = cerditoX - 90F
        ufoY = cerditoY + 165F
        cerditoRect = Rect(cerditoX.toInt()+15, cerditoY.toInt()+45, cerditoX.toInt()+cerditoImage.width-15, cerditoY.toInt()+cerditoImage.height-45)
    }
    // dibujamos los sprites en pantalla
    fun draw(canvas: Canvas){
        canvas.drawBitmap(cerditoImage, cerditoX, cerditoY, null)
        canvas.drawBitmap(ufoImage, ufoX, ufoY, null)
        // canvas.drawRect(cerditoRect, Paint().apply { color = Color.RED })
    }

    /**
     * Actualizamos la ubicación de los sprites Cuando el jugador nos da coordenadas nuevas para mover el cerdito
     */
    fun update(x: Float) {
        cerditoX = x
        ufoX = cerditoX - 90F
        cerditoRect.left = cerditoX.toInt()+15
        cerditoRect.right = cerditoX.toInt()+cerditoImage.width-15
    }
}
