package co.com.gulupagames.gamecore.game.surfaceview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Message
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.entity.IGamePlayer
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity
import co.com.gulupagames.gamecore.game.surfaceview.helpers.*
import co.com.gulupagames.gamecore.game.surfaceview.thread.LevelThread
import co.com.gulupagames.gamecore.model.Rectangle
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class LevelSurfaceView : SurfaceView, SurfaceHolder.Callback {
    enum class LevelEngineMode {
        SEQUENTIAL, RANDOM
    }

    private var lastExecution: Long = 0
    private var minTickTime: Long = 0
    var myCanvasWFixed = 0
        private set
    var myCanvasHFixed = 0
        private set
    var lives = 0
    var level = 0
        private set
    var score = 0
        private set
    var clusterBombs = 0
    private var iniSurfaceView = false
    private var passWorld = false
    private var alive = true
    private var showControls = false
    private var firsTime = false
    private var touch = false
    private var waitForTouch = false
    private var surfaceHolder: SurfaceHolder? = null
    var levelThread: LevelThread? = null
        private set
    var actors: MutableList<GameEntity>? = null
    var iGamePlayer: IGamePlayer? = null
    var canvasView: Canvas? = null
        private set
    var baseGameActivity: BaseGameActivity? = null
    var levelEngineCore: LevelEngineCore? = null
        private set
    private var gameOver: GameOver? = null
    var backGround: BackgroundLooper? = null
        private set
    private var collision: Collision? = null
    private var paintStatus: PaintStatus? = null
    private var levelEngineMode = LevelEngineMode.SEQUENTIAL

    constructor(game: Context?) : super(game) {
        ini()
    }

    constructor(game: Context?, attrs: AttributeSet?) : super(game, attrs) {
        ini()
    }

    constructor(game: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        game,
        attrs,
        defStyle
    ) {
        ini()
    }

    private fun ini() {
        holder.addCallback(this)
        iniSavedGame(0, LIVES_STOCK, 0, true)
        iniSurfaceView = false
        baseGameActivity = context as BaseGameActivity
        backGround = BackgroundLooper(this)
        gameOver = GameOver(this)
        gameOver?.isGameOverPainted = false
        gameOver?.isGameOver = false
        levelEngineCore = LevelEngineCore(this)
        levelEngineCore?.initLevels()
        collision = Collision(this)
        paintStatus = PaintStatus(this)
        clusterBombs = INITIAL_CLUSTER_BOMBS
        minTickTime = 1000 / (FPS + 1)
    }

    private fun iniSavedGame(level: Int, lives: Int, score: Int, passWorld: Boolean) {
        this.level = level
        this.lives = lives
        this.passWorld = passWorld
        this.score = score
    }

    override fun onDraw(canvas: Canvas) {
        levelEngineCore?.let { levelEngineCore ->
            if (iniSurfaceView) {
                canvasView = canvas
                if (gameOver?.isScorePainted == false) {
                    if (gameOver?.isGameOver == false) {
                        baseGameActivity?.hideCentralText()
                        if (!passWorld && alive) {
                            if (!showControls) {
                                baseGameActivity?.showControls()
                                showControls = true
                            }
                            iGamePlayer?.isDeath()
                            playLevel()
                        } else if (!alive) {
                            if (levelEngineMode == LevelEngineMode.RANDOM) {
                                gameOver?.isGameOver = true
                            } else if (levelEngineMode == LevelEngineMode.SEQUENTIAL) {
                                --lives
                                if (lives == 0) {
                                    gameOver?.isGameOver = true
                                } else {
                                    baseGameActivity?.hideControls()
                                    showControls = false
                                    backGround?.paintStage()
                                    alive = true
                                    actors?.clear()
                                    iGamePlayer = null
                                    levelEngineCore.restart()
                                }
                            }
                        } else {
                            if (levelEngineMode == LevelEngineMode.RANDOM) {
                                if (!firsTime) {
                                    val r = Random()
                                    level =
                                        r.nextInt(levelEngineCore.maxLevel - INITIAL_LEVEL) + INITIAL_LEVEL
                                    if (level <= 0) {
                                        level = INITIAL_LEVEL
                                    } else if (level > levelEngineCore.maxLevel) {
                                        level = levelEngineCore.maxLevel
                                    }
                                    backGround?.paintReady()
                                    firsTime = true
                                } else {
                                    gameOver?.isGameOver = true
                                }
                            } else if (levelEngineMode == LevelEngineMode.SEQUENTIAL) {
                                ++level
                                if (level > levelEngineCore.maxLevel) {
                                    gameOver?.isGameOver = true
                                } else {
                                    backGround?.paintStage()
                                    passWorld = false
                                }
                            }
                            initLevel()
                            if (clusterBombs == 0) {
                                clusterBombs = INITIAL_CLUSTER_BOMBS
                            }
                        }
                    } else {
                        when {
                            waitForTouch(gameOver?.isGameOverPainted == true) -> {
                                gameOver?.scorePaint()
                                touch = false
                                waitForTouch = true
                            }
                            gameOver?.isGameOverPainted == false -> {
                                baseGameActivity?.hideControls()
                                showControls = false
                                gameOver?.paintGameOver()
                                actors?.clear()
                                touch = false
                                waitForTouch = true
                            }
                            else -> {
                                gameOver?.paintBlack()
                            }
                        }
                    }
                } else {
                    if (waitForTouch(gameOver?.isScorePainted == true)) {
                        levelThread?.running = false
                        baseGameActivity?.soundCache?.stopLoopSound()
                        baseGameActivity?.backToMainMenu()
                        backGround?.black?.let { canvasView?.drawBitmap(it, 0f, 0f, null) }
                    } else if (gameOver?.isScorePainted == true) {
                        gameOver?.paintBlack()
                    }
                }
            }
        }
    }

    private fun initLevel() {
        levelEngineCore?.initLevel(level)
        actors?.clear()
        iGamePlayer = null
        baseGameActivity?.hideControls()
        showControls = false
        passWorld = false
    }

    private fun playLevel() {
        levelThread?.setSleepTime(FPS_WAIT)
        backGround?.playBackground()
        collision?.checkCollisions()
        levelEngineCore?.updateWorld()
        paintStatus?.paintStatus()
    }

    fun touchOnScreen(x: Int, y: Int) {
        if (waitForTouch) {
            touch = true
        }
        val touchRectangle = Rectangle(x, y, 5, 5)
        collision?.checkTouchCollisions(touchRectangle)
    }

    private fun waitForTouch(condition: Boolean): Boolean {
        if (touch && condition) {
            levelThread?.setSleepTime(FPS_WAIT)
            touch = false
            return true
        }
        return false
    }

    fun setPassWord(passWord: Boolean) {
        passWorld = passWord
    }

    fun setDeath() {
        alive = false
    }

    fun keyPressed(action: Int) {
        if (iGamePlayer != null && showControls) {
            iGamePlayer?.keyPressed(action)
        }
    }

    fun keyPressed(action: Int, curDir: Float) {
        if (iGamePlayer != null && showControls) {
            iGamePlayer?.keyPressed(action, curDir)
        }
    }

    fun keyReleased(action: Int) {
        if (iGamePlayer != null && showControls) {
            iGamePlayer?.keyReleased(action)
        }
    }

    fun addActor(entity: GameEntity) {
        actors?.add(entity)
    }

    fun addActors(newActors: MutableList<GameEntity>) {
        actors?.addAll(newActors)
    }


    fun addScore(score: Int) {
        if (this.score + score < 0) {
            this.score = 0
        } else {
            this.score += score
        }
        val message = Message()
        message.arg1 = this.score
        baseGameActivity?.updateScoreHandler?.sendMessage(message)
    }

    fun addClusterBombs(clusterBombs: Int) {
        this.clusterBombs += clusterBombs
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // SURFACE VIEW METHODS
    /////////////////////////////////////////////////////////////////////////////////////////
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceCreated(holder: SurfaceHolder) {
        myCanvasWFixed = width
        myCanvasHFixed = height
        if (myCanvasWFixed < myCanvasHFixed) {
            myCanvasWFixed = height
            myCanvasHFixed = width
        }
        val myCanvas = Canvas()
        myCanvas.setBitmap(
            baseGameActivity?.spriteCache?.getSpriteCanvas(
                myCanvasWFixed,
                myCanvasHFixed
            )
        )
        actors = CopyOnWriteArrayList()
        backGround?.loadBackGround()
        iniSurfaceView = true
        if (baseGameActivity?.isPlayGame == true && baseGameActivity?.isPause == false) {
            levelSurfaceViewOnResume()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        iniSurfaceView = false
    }

    fun levelSurfaceViewOnResume() {
        if (levelThread == null || levelThread?.running == false) {
            surfaceHolder = holder
            holder.addCallback(this)
            levelThread = LevelThread(this)
            levelThread?.running = true
            levelThread?.start()
            baseGameActivity?.soundCache?.startLoopSound()
        }
    }

    fun levelSurfaceViewOnPause() {
        if (levelThread != null && levelThread?.running == true) {
            var retry = true
            levelThread?.running = false
            while (retry) {
                try {
                    levelThread?.join()
                    retry = false
                } catch (e: InterruptedException) {
                    Log.e("ERROR", "error pausing levelSurfaceView", e)
                }
            }
            baseGameActivity?.soundCache?.pauseLoopSound()
        }
    }

    @SuppressLint("WrongCall")
    fun updateSurfaceView() {
        var execute = false
        if (lastExecution == 0L) {
            execute = true
            lastExecution = System.currentTimeMillis()
        }
        val actualTime = System.currentTimeMillis()
        if (actualTime - lastExecution > minTickTime) {
            execute = true
            lastExecution = System.currentTimeMillis()
        }
        if (execute) {
            var canvas: Canvas? = null
            try {
                surfaceHolder?.let {
                    canvas = it.lockCanvas()
                    canvas?.let { canvas ->
                        synchronized(it) { onDraw(canvas) }
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder?.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    companion object {
        const val LEFT = 0
        const val RIGHT = 1
        const val UP = 2
        const val DOWN = 3
        const val LASER = 4
        const val BOMB = 5
        const val CHANGE_LEVEL_PAUSE_TIME: Long = 4000
        const val FPS_WAIT: Long = 0
        private const val FPS: Long = 62
        private const val LIVES_STOCK = 3
        private const val INITIAL_LEVEL = 1
        private const val INITIAL_CLUSTER_BOMBS = 5
    }
}