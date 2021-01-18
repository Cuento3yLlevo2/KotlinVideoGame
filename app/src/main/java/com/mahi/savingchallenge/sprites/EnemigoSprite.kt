package com.mahi.savingchallenge.sprites

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.MediaPlayer
import com.mahi.savingchallenge.R
import kotlin.random.Random

/**
 * Clase que gestiona todos los enemigos
 */
class EnemigoSprite(bmp: Bitmap, var enemyType: Int, context: Context) {

    // Sonido enemigo intersectado
    private var enemyIntersectedSound : MediaPlayer = MediaPlayer()
    private var gameViewContext = context
    // cargamos imagen de enemigo
    private var enemigoImage: Bitmap = bmp
    // pre cargamos coordenadas
    private var enemigoX : Float
    private var enemigoY : Float
    var enemigoRect : Rect
    private var leftHitboxArrangement: Int = 0
    private var topHitboxArrangement: Int = 0
    private var rightHitboxArrangement: Int = 0
    private var bottomHitboxArrangement: Int = 0
    private var enemySpeed: Int = 0
    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        // cargamos sonido de enemigo cuando colisiona
        enemyIntersectedSound = MediaPlayer.create(gameViewContext, R.raw.enemy_intersected_sound)
        enemyIntersectedSound.setVolume(0.8F, 0.8F)
        // cargamos coordenadas random para el inicio de pintado de el enemigo
        enemigoX = Random.nextInt(40, screenWidth - 80).toFloat()
        enemigoY = Random.nextInt(-3000, - 400).toFloat()
        // Dependiendo de el tipo de enemigo modificamos el tamaño de el hitbox
        when (enemyType) {
            HAMBURGER -> {
                leftHitboxArrangement = 8
                topHitboxArrangement = 20
                rightHitboxArrangement = 8
                bottomHitboxArrangement = 20
                enemySpeed = 25
            }
            COFFEE -> {
                leftHitboxArrangement = 24
                topHitboxArrangement = 9
                rightHitboxArrangement = 24
                bottomHitboxArrangement = 9
                enemySpeed = 16
            }
            COOKIE -> {
                leftHitboxArrangement = 9
                topHitboxArrangement = 9
                rightHitboxArrangement = 9
                bottomHitboxArrangement = 9
                enemySpeed = 12
            }
            PIZZA -> {
                leftHitboxArrangement = 16
                topHitboxArrangement = 84
                rightHitboxArrangement = 16
                bottomHitboxArrangement = 36
                enemySpeed = 25
            }
        }
        // enemigoRect es el hitbox para detectar las colisiones
        enemigoRect = Rect(enemigoX.toInt()+leftHitboxArrangement, enemigoY.toInt()+topHitboxArrangement, enemigoX.toInt()+enemigoImage.width-rightHitboxArrangement, enemigoY.toInt()+enemigoImage.height-bottomHitboxArrangement)
    }
    // pintamos el enemigo en pantalla
    fun draw(canvas: Canvas){
        canvas.drawBitmap(enemigoImage, enemigoX, enemigoY, null)
        // canvas.drawRect solo útil para pruebas con hitbox visible
        //canvas.drawRect(enemigoRect, Paint().apply { color = Color.YELLOW })
    }

    /**
     * Se encarga de mover el enemigo hacia abajo cada repintado de pantalla
     * si el enemigo baja al final de la pantalla sin colisión se reinicia su ubicación
     */
    fun update() {
        if (enemigoY < screenHeight) {
            moveDownEnemy()
        } else {
            resetEnemy()
        }
    }

    /**
     * Se encarga de reiniciar las coordenadas del enemigo si hay colisión con el cerdito.
     * reproduce el sonido de enemigo colisionado
     */
    fun resetIfIntersected(intersectedEnemy: Rect?) {
        if (intersectedEnemy != null) {
            if (intersectedEnemy.intersect(this.enemigoRect)){
                enemyIntersectedSound.start()
                resetEnemy()
            }
        }
    }

    /**
     * Se encarga de repintar el enemigo en la pantalla dependiendo de la velocidad del enemigo
     */
    private fun moveDownEnemy() {
        enemigoY += enemySpeed
        enemigoRect.top = enemigoY.toInt()+topHitboxArrangement
        enemigoRect.bottom = enemigoY.toInt()+enemigoImage.height-bottomHitboxArrangement
    }

    /**
     * se encarga se reiniciar la ubicación del enemigo en pantalla
     * con coordenadas random ubicamos el enemigo arriba de la pantalla
     */
    private fun resetEnemy() {
        enemigoY = Random.nextInt(-2200, - 120).toFloat()
        enemigoX = Random.nextInt(40, screenWidth - 80).toFloat()
        enemigoRect.set(enemigoX.toInt()+leftHitboxArrangement, enemigoY.toInt()+topHitboxArrangement, enemigoX.toInt()+enemigoImage.width-rightHitboxArrangement, enemigoY.toInt()+enemigoImage.height-bottomHitboxArrangement)
    }

    /**
     * Cada constante representa un tipo de enemigo
     */
    companion object {
        // tipos de enemigo
        const val HAMBURGER: Int = 1
        const val COFFEE: Int = 2
        const val COOKIE: Int = 3
        const val PIZZA: Int = 4
    }
}