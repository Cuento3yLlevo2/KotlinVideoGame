package com.mahi.savingchallenge

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity que se muestra cuando el jugador pierde la partida
 */
class GameOverActivity : AppCompatActivity() {
    // musica de game over
    private var musicGameOver : MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        setSupportActionBar(findViewById(R.id.game_over_toolbar))
        // Esta clase pide al jugador que vuelva a el inicio para volver a jugar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Volver al inicio"
    }
    override fun onStart() {
        super.onStart()
        musicGameOver = MediaPlayer.create(applicationContext, R.raw.long_game_over_notification)
        musicGameOver.setVolume(0.5F, 0.5F)
        musicGameOver.start()
    }
    override fun onBackPressed() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}