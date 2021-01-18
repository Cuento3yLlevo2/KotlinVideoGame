package com.mahi.savingchallenge

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * MainActivity permite al jugador elegir el tipo de dificultad y ver sus puntuaciones
 */
class MainActivity : AppCompatActivity() {
    // Sonido principal de el juego
    private var musicIntro : MediaPlayer = MediaPlayer()
    // RadioButtons para controlar si el juego es modo fácil, normal, difícil
    private lateinit var rbEasyMode: AppCompatRadioButton
    private lateinit var rbNormalMode: AppCompatRadioButton
    private lateinit var rbHardMode: AppCompatRadioButton
    private lateinit var radioGroupGameMode: RadioGroup
    // Button para iniciar el juego
    private lateinit var bMainPlayBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // cargamos los elementos de xml
        radioGroupGameMode = findViewById(R.id.radioGroupGameMode)
        rbEasyMode = findViewById(R.id.rbEasyMode)
        rbNormalMode = findViewById(R.id.rbNormalMode)
        rbHardMode = findViewById(R.id.rbHardMode)
        bMainPlayBtn = findViewById(R.id.bMainPlayBtn)

        /*
        cargamos lógica de cuando el jugador hace click en el buttom
        detectamos el tipo de dificultad y iniciamos el juego
        */
        bMainPlayBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            var gameMode = "2"
            when(radioGroupGameMode.checkedRadioButtonId){
                R.id.rbEasyMode -> gameMode = "1"
                R.id.rbNormalMode -> gameMode = "2"
                R.id.rbHardMode -> gameMode = "3"
            }
            intent.putExtra("GAME_MODE", gameMode)
            startActivity(intent)
        }
    }

    /**
     * Controla los estilos de los radio buttoms
     */
    fun onRadioButtonClicked(view: View) {
        val isSelected: Boolean = (view as AppCompatRadioButton).isChecked
        when(view.id){
            R.id.rbEasyMode -> if (isSelected){
                rbEasyMode.setTextColor(Color.WHITE)
                rbNormalMode.setTextColor(resources.getColor(R.color.blue, theme))
                rbHardMode.setTextColor(resources.getColor(R.color.red, theme))
            }
            R.id.rbNormalMode -> if (isSelected){
                rbEasyMode.setTextColor(resources.getColor(R.color.green, theme))
                rbNormalMode.setTextColor(Color.WHITE)
                rbHardMode.setTextColor(resources.getColor(R.color.red, theme))
                }
            R.id.rbHardMode -> if (isSelected){
                rbEasyMode.setTextColor(resources.getColor(R.color.green, theme))
                rbNormalMode.setTextColor(resources.getColor(R.color.blue, theme))
                rbHardMode.setTextColor(Color.WHITE)
            }
        }
    }

    /**
     * iniciamos el sonido de música de intro
     */
    override fun onStart() {
        super.onStart()
        musicIntro = MediaPlayer.create(applicationContext, R.raw.intro_percussion)
        musicIntro.isLooping = true
        musicIntro.setVolume(0.2F, 0.2F)
        musicIntro.start()
    }
    override fun onPause() {
        super.onPause()
        musicIntro.pause()
    }
    override fun onStop() {
        musicIntro.stop()
        super.onStop()
    }

}