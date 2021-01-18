package com.mahi.savingchallenge.controllers

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer

/**
 * Esta clase se encargada de gestionar el cronometro de cuenta atrás para un minuto
 */
class CountDownGameTimer {

    private var paintTimer : Paint = Paint()
    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    // Iniciamos el contador a 60 secundos
    private var startMiliSeconds = 60000L

    private lateinit var countdownTimer: CountDownTimer
    var isRunning: Boolean = false
    var timeInMilliSeconds = 0L
    var timerText: String = "0:00"

    init {
        startTimer(startMiliSeconds)
        paintTimer.textAlign = Paint.Align.LEFT
        paintTimer.textSize = 100F
        paintTimer.color = Color.WHITE
    }

    // Dibujamos el contador en la esquina superior derecha de la pantalla.
    fun draw(canvas: Canvas){
        canvas.drawText(timerText, screenWidth.toFloat() - 280, 150F, paintTimer)
    }

    // esta función detecta cuando el contador finaliza y actualiza el texto en pantalla
    private fun startTimer(time_in_seconds: Long) {
        countdownTimer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                isRunning = false
            }
            override fun onTick(p0: Long) {
                timeInMilliSeconds = p0
                updateTextUI()
            }
        }
        countdownTimer.start()
        isRunning = true
    }
    // funcion que actualiza lo que se muestra en pantalla
    private fun updateTextUI() {
        val minute = (timeInMilliSeconds / 1000) / 60
        val seconds = (timeInMilliSeconds / 1000) % 60
        timerText = "$minute:$seconds"
    }

}