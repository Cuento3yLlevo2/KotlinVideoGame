package com.mahi.savingchallenge.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.SurfaceView
import androidx.core.content.ContextCompat.startActivity
import com.mahi.savingchallenge.GameOverActivity
import com.mahi.savingchallenge.PlayerWinsActivity
import com.mahi.savingchallenge.R
import com.mahi.savingchallenge.sprites.BackgroundImage
import com.mahi.savingchallenge.sprites.CerditoSprite
import com.mahi.savingchallenge.sprites.EnemigoSprite
import com.mahi.savingchallenge.sprites.MonedaSprite

/**
 * Esta clase gestiona el hilo principal de ejecución de el juego
 */
@SuppressLint("ViewConstructor")
class GameView(private val gamecontext: Context, private val GAME_MODE: Int): SurfaceView(gamecontext), Callback  {

    // var para gestionar subida de nivel durante el juego
    private var upTo30Seconds: Boolean = false
    // hilo de ejecución
    private var thread: GameLoop
    // Imagen de fondo
    private lateinit var backgroundImage: BackgroundImage
    // Cerdito Sprite
    private lateinit var cerditoSprite: CerditoSprite
    // Monedas Sprites
    private lateinit var monedaSprite1: MonedaSprite
    private lateinit var monedaSprite2: MonedaSprite
    private lateinit var monedaSprite3: MonedaSprite
    private lateinit var bagMonedasSprite1: MonedaSprite
    // Enemigos Sprites
    private lateinit var enemigoHamburgerSprite: EnemigoSprite
    private lateinit var enemigoCoffeeSprite: EnemigoSprite
    private lateinit var enemigoCookieSprite: EnemigoSprite
    // Enemigos Sprites nivel normal y difícil
    private lateinit var enemigoHamburgerSprite2: EnemigoSprite
    private lateinit var enemigoCoffeeSprite2: EnemigoSprite
    private lateinit var enemigoCookieSprite2: EnemigoSprite
    private lateinit var enemigoPizzaSprite: EnemigoSprite
    // Var para gestionar el contador de monedas
    private var scoreBar: ScoreBar = ScoreBar()
    // Var para gestionar el cronometro
    private var countDownGameTimer: CountDownGameTimer = CountDownGameTimer()
    // rect para gestionar si una moneda/enemy es interceptada
    private var intersectedCoin : Rect? = null
    private var intersectedEnemy : Rect? = null
    // ubicacion X de el cerdito
    private var cerditoX = 200F

    init {
        // Preparo Hilo de ejecución
        holder.addCallback(this)
        thread = GameLoop(holder, this)
        isFocusable = true
    }

    /***
     * Si cambia lo que se ve en la pantalla se ejecuta este método pero no es útil para el juego
     */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // do nothing
    }

    /***
     * Pre-carga los sprite en pantalla e inicia el hilo de ejecución
     */
    override fun surfaceCreated(holder: SurfaceHolder) {
        // Cargamos Fondo de pantalla
        backgroundImage = BackgroundImage(BitmapFactory.decodeResource(resources, R.drawable.city_background))
        // Cargamos Cerdito
        cerditoSprite = CerditoSprite(BitmapFactory.decodeResource(resources, R.drawable.cerdito), BitmapFactory.decodeResource(resources, R.drawable.ufo))
        // Cargamos Monedas
        val monedaImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.euro_coin)
        val bagMonedasImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bag_of_coins)
        monedaSprite1 = MonedaSprite(monedaImage, 1, context)
        monedaSprite2 = MonedaSprite(monedaImage, 1,  context)
        monedaSprite3 = MonedaSprite(monedaImage, 1,  context)
        bagMonedasSprite1 = MonedaSprite(bagMonedasImage, 2,  context)
        // Cargamos Enemigos
        enemigoHamburgerSprite = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.hamburger), 1, context)
        enemigoCoffeeSprite = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.coffee), 2, context)
        enemigoCookieSprite = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.cookie), 3, context)
        enemigoHamburgerSprite2 = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.hamburger), 1, context)
        enemigoCoffeeSprite2 = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.coffee), 2, context)
        enemigoCookieSprite2 = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.cookie), 3, context)
        enemigoPizzaSprite = EnemigoSprite(BitmapFactory.decodeResource(resources, R.drawable.pizza), 4, context)

        // Inicio Hilo de ejecución
        thread.running = true
        thread.start()
    }

    /***
     * Se encarga de destruir el hilo de ejecución
     */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.running = false
                thread.join()

            } catch (e: InterruptedException){
                e.printStackTrace()
            }
            retry = false
        }

    }

    /***
     * Se ejecuta Cada vez que se re-pinta la pantalla
     * actualizamos los elementos en pantalla y detectamos colisiones
     * Actualizamos las monedas y tiempo en cronometro
     * detectamos si el jugador pierde o gana en este método.
     */
    fun update() {
        manageIntersections()
        cerditoSprite.update(cerditoX)
        monedaSprite1.update()
        monedaSprite2.update()
        monedaSprite3.update()
        bagMonedasSprite1.update()
        enemigoHamburgerSprite.update()
        enemigoCoffeeSprite.update()
        enemigoCookieSprite.update()
        manageGameOver()
        manageLevelUP()
        managePlayerWins()
    }

    /***
     * Método se encarga de detectar si el jugador gana
     * * El jugador gana la partida si logra sobrevivir un minuto con monedas
     */
    private fun managePlayerWins() {
        if(!countDownGameTimer.isRunning){
            thread.running = false
            playerWins()
        }
    }

    /**
     * Método se encarga de detectar si el jugador pierde
     * * El jugador pierde si se queda sin monedas antes de un minuto
     */
    private fun manageGameOver() {
        if(scoreBar.score < 0){
            thread.running = false
            gameOver()
        }
    }

    /**
     * se encarga de subir la dificultad de el juego dependiendo de el nivel de dificultad seleccionado.
     */
    private fun manageLevelUP() {
        when(GAME_MODE){
            EASY_MODE -> {
                // EASY_MODE no tenemos enemigo pizza
                enemigoHamburgerSprite2.update()
            }
            NORMAL_MODE -> {
                enemigoHamburgerSprite2.update()
                if (upTo30Seconds){
                    enemigoPizzaSprite.update()
                    enemigoCookieSprite2.update()
                } else {
                    if (countDownGameTimer.timerText.contentEquals("0:30")){
                        upTo30Seconds = true
                    }
                }
            }
            HARD_MODE -> {
                enemigoPizzaSprite.update()
                enemigoHamburgerSprite2.update()
                enemigoCoffeeSprite2.update()
                enemigoCookieSprite2.update()
            }
        }
    }

    /**
     * Se encarga de gestionar las colisiones entre el cerdito, monedas, enemigos.
     */
    private fun manageIntersections() {
        // Detecta si una moneda ha sido interceptada
        intersectedCoin = scoreBar.updateMonedaCerditoIntersections(
                arrayListOf(monedaSprite1.monedaRect, monedaSprite2.monedaRect, monedaSprite3.monedaRect, bagMonedasSprite1.monedaRect),
                cerditoSprite.cerditoRect,
                arrayListOf(monedaSprite1.monedaType, monedaSprite2.monedaType, monedaSprite3.monedaType, bagMonedasSprite1.monedaType)
        )
        if (intersectedCoin!=null){
            monedaSprite1.resetIfIntersected(intersectedCoin)
            monedaSprite2.resetIfIntersected(intersectedCoin)
            monedaSprite3.resetIfIntersected(intersectedCoin)
            bagMonedasSprite1.resetIfIntersected(intersectedCoin)
        }
        // Detecta si un enemigo a sido interceptado
        intersectedEnemy = scoreBar.updateEnemigoCerditoIntersections(
                arrayListOf(enemigoHamburgerSprite.enemigoRect, enemigoCoffeeSprite.enemigoRect, enemigoCookieSprite.enemigoRect, enemigoHamburgerSprite2.enemigoRect, enemigoCoffeeSprite2.enemigoRect, enemigoCookieSprite2.enemigoRect, enemigoPizzaSprite.enemigoRect),
                cerditoSprite.cerditoRect,
                arrayListOf(enemigoHamburgerSprite.enemyType, enemigoCoffeeSprite.enemyType, enemigoCookieSprite.enemyType, enemigoHamburgerSprite2.enemyType, enemigoCoffeeSprite2.enemyType, enemigoCookieSprite2.enemyType, enemigoPizzaSprite.enemyType)
        )
        if (intersectedEnemy!=null){
            enemigoHamburgerSprite.resetIfIntersected(intersectedEnemy)
            enemigoCoffeeSprite.resetIfIntersected(intersectedEnemy)
            enemigoCookieSprite.resetIfIntersected(intersectedEnemy)
            enemigoHamburgerSprite2.resetIfIntersected(intersectedEnemy)
            enemigoCoffeeSprite2.resetIfIntersected(intersectedEnemy)
            enemigoCookieSprite2.resetIfIntersected(intersectedEnemy)
            enemigoPizzaSprite.resetIfIntersected(intersectedEnemy)
        }
        // volvemos a recalcular
        intersectedCoin = null
        intersectedEnemy = null
    }

    /**
     * Se encarga de dibujar los elementos en el hilo de ejecución
     */
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(canvas!=null){
            backgroundImage.draw(canvas)
            cerditoSprite.draw(canvas)
            monedaSprite1.draw(canvas)
            monedaSprite2.draw(canvas)
            monedaSprite3.draw(canvas)
            bagMonedasSprite1.draw(canvas)
            enemigoHamburgerSprite.draw(canvas)
            enemigoCoffeeSprite.draw(canvas)
            enemigoCookieSprite.draw(canvas)
            enemigoHamburgerSprite2.draw(canvas)
            enemigoCoffeeSprite2.draw(canvas)
            enemigoCookieSprite2.draw(canvas)
            enemigoPizzaSprite.draw(canvas)
            scoreBar.draw(canvas)
            countDownGameTimer.draw(canvas)
        }
    }

    /**
     * Se encarga de detectar cuando el usuario mueve el cerdito para mover el cerdito
     * a la posición deseada
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE){
            if (event.x < width - 185 && event.x > 20) {
                cerditoX = event.x
                performClick()
            }
        }
        return true
    }

    /**
     * Si el jugador pierde muestra la actividad de game over
     */
    private fun gameOver() {
        val intent = Intent(gamecontext, GameOverActivity::class.java)
        startActivity(gamecontext, intent, null)
    }

    /**
     * Si el jugador gana muestra la actividad de Player Wins
     */
    private fun playerWins() {
        val intent = Intent(gamecontext, PlayerWinsActivity::class.java)
        intent.putExtra("score", scoreBar.score.toString())
        startActivity(gamecontext, intent, null)
    }

    /**
     * Variables Constantes que determinan si el jugador a elegido dificultad
     * fácil
     * normal
     * difícil
     */
    companion object {
        const val EASY_MODE: Int = 1
        const val NORMAL_MODE: Int = 2
        const val HARD_MODE: Int = 3
    }

}
