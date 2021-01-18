package com.mahi.savingchallenge

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import com.mahi.savingchallenge.controllers.GameView

/**
 * Clase intermediaria que ejecuta el hilo de ejecución principal de el juego.
 */
class GameActivity : Activity() {
    // Sonido principal de el juego
    private var musicLoop : MediaPlayer = MediaPlayer()
    private lateinit var gameView: GameView
    private var musicCurrentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
           esta clase recibe de el MainActivity el modo de juego
           (fácil, normal, difícil)
           y le pasa como parámetro a gameView la información
         */
        val gameMode = intent.getStringExtra("GAME_MODE")
        gameView = GameView(this, gameMode?.toInt() ?: 2)
        setContentView(gameView)
    }
    // cuando inicia esta actividad reproduce el sonido de juego
    override fun onStart() {
        super.onStart()
        musicLoop = MediaPlayer.create(applicationContext, R.raw.main_game_song_loop)
        musicLoop.isLooping = true
        musicLoop.seekTo(musicCurrentPosition)
        musicLoop.setVolume(0.2F, 0.2F)
        musicLoop.start()
    }
    override fun onPause() {
        super.onPause()
        musicLoop.pause()
        musicCurrentPosition = musicLoop.currentPosition
    }
    override fun onStop() {
        musicLoop.stop()
        super.onStop()
        finish()
    }
}