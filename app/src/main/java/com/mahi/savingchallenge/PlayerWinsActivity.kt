package com.mahi.savingchallenge

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

/**
 * Activity que se muestra cuando el jugador gana una partida
 */
class PlayerWinsActivity : AppCompatActivity() {
    // musica de reto completado
    private var musicGameOver : MediaPlayer = MediaPlayer()
    // gestor de efectos de confeti
    private lateinit var viewKonfettiView: KonfettiView
    // carga la cantidad de monedas recolectadas
    private lateinit var playerScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_wins)
        setSupportActionBar(findViewById(R.id.player_wins_toolbar))
        // el toolbar pide al jugador volver al inicio para volver a jugar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Volver al inicio"
        playerScore = findViewById(R.id.tvPlayerScoreValue)
        playerScore.text = intent.getStringExtra("score")
        viewKonfettiView = findViewById(R.id.viewKonfetti)
    }

    /**
     * Al iniciar la actividad cargamos confeti como efecto visuaL
     *  iniciamos efecto de sonido de victoria
     */
    override fun onStart() {
        super.onStart()
        loadConfeti()
        musicGameOver = MediaPlayer.create(applicationContext, R.raw.player_wins)
        musicGameOver.setVolume(0.5F, 0.5F)
        musicGameOver.start()
    }
    override fun onBackPressed() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Lógica y coordenada para confeti (efecto visual de victoria)
     */
    private fun loadConfeti() {
        viewKonfettiView.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfettiView.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }
}