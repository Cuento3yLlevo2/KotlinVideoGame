package com.mahi.savingchallenge.controllers

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder

/**
 * Clase que gestiona los FPS de los Hilos de ejecución
 */
class GameLoop(private var surfaceHolder: SurfaceHolder, private var gameView: GameView) : Thread() {

    // establecemos una velocidad esperada de FPS
    private var gameCanvas: Canvas? = null
    private var targetFPS: Int = 60
    private var averageFPS: Double = 0.0
    var running: Boolean = false

    /**
     * El método run se encarga de ejecutar los hilos en la frecuencia deseada
     */
    override fun run() {
        super.run()
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        var totalTime: Long = 0
        var frameCount = 0
        val targetTime: Int = 1000/ targetFPS
        // La taza de refresco de pantalla la intentamos mantener en 60FPS
        while (running) {
            startTime = System.nanoTime()
            gameCanvas = null

            try {
                gameCanvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder){
                    this.gameView.update()
                    this.gameView.draw(gameCanvas)
                }
            } catch (e: Exception){
            } finally {
                if (gameCanvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(gameCanvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            try {
                sleep(waitTime)
            } catch (e: Exception){e.printStackTrace()}

            totalTime += System.nanoTime() - startTime
            frameCount++
            if (frameCount == targetFPS) {
                averageFPS = (1000 / ((totalTime / frameCount) / 1000000 )).toDouble()
                frameCount = 0
                totalTime = 0
                Log.d("averageFPS", "averageFPS = $averageFPS")
            }

        }
    }

}
