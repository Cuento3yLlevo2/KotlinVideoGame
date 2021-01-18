package com.mahi.savingchallenge.controllers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.mahi.savingchallenge.sprites.EnemigoSprite.Companion.COFFEE
import com.mahi.savingchallenge.sprites.EnemigoSprite.Companion.COOKIE
import com.mahi.savingchallenge.sprites.EnemigoSprite.Companion.HAMBURGER
import com.mahi.savingchallenge.sprites.EnemigoSprite.Companion.PIZZA
import com.mahi.savingchallenge.sprites.MonedaSprite.Companion.BAG
import com.mahi.savingchallenge.sprites.MonedaSprite.Companion.COIN

/**
 * Clase que gestiona el contador de monedas
 */
class ScoreBar {
    private var paintScoreBar : Paint = Paint()
    var score : Int = 0
    private var enemyType = 1
    private var monedaType = 1
    // Al inicio de el juego el jugador comienza con 0 monedas
    init {
        score = 0
        paintScoreBar.textAlign = Paint.Align.RIGHT
        paintScoreBar.textSize = 100F
        paintScoreBar.color = Color.YELLOW
    }
    // Dibujamos el contador de monedas en la esquina superior izquierda de la pantalla
    fun draw(canvas: Canvas){
        canvas.drawText(score.toString(), 150F, 150F, paintScoreBar)
    }

    /**
     * Si detectamos la colisión con un moneda sumamos al contador
     * 1 si es una moneda normal y 10 si es una bolsa de monedas
     */
    fun updateMonedaCerditoIntersections(arrayListOfMonedaRect: ArrayList<Rect>, cerditoRect: Rect, arrayListOfMonedaTypes: ArrayList<Int>): Rect? {
        for ((index, monedaRect: Rect) in arrayListOfMonedaRect.withIndex()) {
            monedaType = arrayListOfMonedaTypes[index]
            if (monedaRect.intersect(cerditoRect)){

                when (monedaType) {
                    COIN -> {
                        score += 1
                    }
                    BAG -> {
                        score += 10
                    }
                }
                return monedaRect
            }
        }
        return null
    }


    /**
     * Si detectamos la colisión con un enemigo restamos al contador
     * enemigo hamburger -> restamos 10
     * enemigo coffee -> restamos 5
     * enemigo cookie -> restamos 4
     * enemigo pizza -> restamos Todas las monedas (Game Over)
     */
    fun updateEnemigoCerditoIntersections(arrayListOfenemyRect: ArrayList<Rect>, cerditoRect: Rect, arrayListOfEnemyTypes: ArrayList<Int>): Rect? {
        for ((index, enemyRect: Rect) in arrayListOfenemyRect.withIndex()) {
            enemyType = arrayListOfEnemyTypes[index]
            if (enemyRect.intersect(cerditoRect)){
                when (enemyType) {
                    HAMBURGER -> {
                        score -= 10
                    }
                    COFFEE -> {
                        score -= 5
                    }
                    COOKIE -> {
                        score -= 4
                    }
                    PIZZA -> {
                        score = -1
                    }
                }
                return enemyRect
            }
        }
        return null
    }

}
