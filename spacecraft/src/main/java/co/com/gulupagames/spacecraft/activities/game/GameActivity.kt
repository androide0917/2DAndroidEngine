package co.com.gulupagames.spacecraft.activities.game

import android.graphics.Bitmap
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.com.gulupagames.gamecore.accelerometer.AccelerometerSensor
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.game.surfaceview.helpers.BackgroundLooper.BackGroundAnimMode
import co.com.gulupagames.gamecore.util.GameImagesResourceLoader
import co.com.gulupagames.gamecore.util.Util
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.dialogfragments.BackToMainMenuDialogFragment
import co.com.gulupagames.spacecraft.dialogfragments.ExitDialogFragment
import co.com.gulupagames.spacecraft.entity.entity.GamePlayer
import co.com.gulupagames.spacecraft.levelengine.LevelEngine

class GameActivity : BaseGameActivity(), View.OnClickListener {
    private var playButton: Button? = null
    private var showAchievementsButton: Button? = null
    private var showLeaderBoardButton: Button? = null
    private var exitButton: Button? = null
    private var levelPercent: ProgressBar? = null
    private var shields: ProgressBar? = null
    private var scoreText: TextView? = null
    private var lives: TextView? = null
    private var bombs: TextView? = null
    private var accelerometerSensor: AccelerometerSensor? = null
    private var leftPanel: LinearLayout? = null
    private var rightPanel: LinearLayout? = null
    private var backToMainMenuDialogFragment: BackToMainMenuDialogFragment? = null
    private val showControlsHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            leftPanel?.visibility = View.VISIBLE
            rightPanel?.visibility = View.VISIBLE
        }
    }
    private val hideControlsHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            leftPanel?.visibility = View.GONE
            rightPanel?.visibility = View.GONE
        }
    }
    override val updateScoreHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val score = msg.arg1.toString() + ""
            scoreText?.text = score
        }
    }

    override val updateLevelPercentHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.arg1 > 100) {
                msg.arg1 = 100
            }
            levelPercent?.progress = msg.arg1
        }
    }
    override val updateLivesHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.arg1 < 0) {
                msg.arg1 = 0
            }
            val lives = msg.arg1.toString() + ""
            this@GameActivity.lives?.text = lives
        }
    }
    override val updateBombsHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.arg1 < 0) {
                msg.arg1 = 0
            }
            val bombs = msg.arg1.toString() + ""
            this@GameActivity.bombs?.text = bombs
        }
    }
    override val updateShieldsHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.arg1 < 0) {
                msg.arg1 = 0
            }
            shields?.progress = msg.arg1
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        levelEngine = LevelEngine(this)
        if (savedInstanceState != null) {
            imageLoader = loaderManager.getLoader<Any>(IMAGE_LOADER) as GameImagesResourceLoader
        } else {
            loaderManager.initLoader(IMAGE_LOADER, null, this)
        }
        gameBegin()
    }

    override fun onClick(v: View) {
        if (v == playButton) {
            runGame()
        } else if (v == showAchievementsButton) {
            //showAchievements();
        } else if (v == showLeaderBoardButton) {
            //showLeaderBoard();
        } else if (v == exitButton) {
            showExitDialogFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        sm.unregisterListener(accelerometerSensor)
    }

    override fun getLevelBackGroundAnimMode(level: Int): BackGroundAnimMode {
        return BackGroundAnimMode.UP_TO_DOWN_ANIM
    }

    override fun getLevelTransitionFontColor(): Int {
        return Color.WHITE
    }

    override fun showControls() {
        showControlsHandler.sendMessage(Message())
    }

    override fun hideControls() {
        hideControlsHandler.sendMessage(Message())
    }

    override fun getLevelBackGround(level: Int): Bitmap? {
        spriteCache?.apply {
            levelSurfaceView?.let {
                return getSpriteMaxSize(
                    R.drawable.fondonivel,
                    it.myCanvasWFixed,
                    it.myCanvasHFixed
                )
            }
        }
        return null
    }

    override fun getLoopSound(): MediaPlayer {
        return MediaPlayer.create(this, R.raw.music)
    }

    override fun runMainMenu() {
        super.runMainMenu()
        setContentView(R.layout.game)
        viewGroup = findViewById<View>(R.id.game_content) as ViewGroup
        layoutInflater.inflate(R.layout.menu, viewGroup, true)
        mainMenuLayout = findViewById<View>(R.id.options_layout_menu) as LinearLayout
        menuRelativeLayout = findViewById<View>(R.id.base_layout_menu) as RelativeLayout
        playButton = findViewById<View>(R.id.play_button) as Button
        showAchievementsButton = findViewById<View>(R.id.showAchievementsButton) as Button
        showLeaderBoardButton = findViewById<View>(R.id.showLeaderBoardButton) as Button
        exitButton = findViewById<View>(R.id.exit_button) as Button
        playButton?.setOnClickListener(this)
        showAchievementsButton?.setOnClickListener(this)
        showLeaderBoardButton?.setOnClickListener(this)
        exitButton?.setOnClickListener(this)
    }

    override fun runGame() {
        super.runGame()
        setContentView(R.layout.level)
        val levelRootLayout = findViewById<View>(R.id.level_root_layout) as RelativeLayout
        leftPanel = findViewById<View>(R.id.left_panel) as LinearLayout
        rightPanel = findViewById<View>(R.id.right_panel) as LinearLayout
        levelSurfaceView = findViewById<View>(R.id.level_surface_view) as LevelSurfaceView
        centralTextView = findViewById<View>(R.id.central_text) as TextView
        val laserButton = findViewById<View>(R.id.laser) as Button
        val bombButton = findViewById<View>(R.id.bomb) as Button
        val pauseButton = findViewById<View>(R.id.pause) as Button
        scoreText = findViewById<View>(R.id.score_txt) as TextView
        levelPercent = findViewById<View>(R.id.percent_txt) as ProgressBar
        lives = findViewById<View>(R.id.lives_txt) as TextView
        shields = findViewById<View>(R.id.shields_txt) as ProgressBar
        shields?.max = GamePlayer.MAX_SHIELDS
        bombs = findViewById<View>(R.id.bombs_txt) as TextView
        levelSurfaceView?.baseGameActivity = this
        centralTextView?.visibility = View.GONE
        laserButton.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                levelSurfaceView?.keyPressed(LevelSurfaceView.LASER)
            } else if (event.action == MotionEvent.ACTION_UP) {
                levelSurfaceView?.keyReleased(LevelSurfaceView.LASER)
            }
            true
        }
        bombButton.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                levelSurfaceView?.keyPressed(LevelSurfaceView.BOMB)
            } else if (event.action == MotionEvent.ACTION_UP) {
                levelSurfaceView?.keyReleased(LevelSurfaceView.BOMB)
            }
            true
        }
        pauseButton.setOnClickListener {
            if (isPlayGame) {
                pauseGame(true)
            }
        }
        levelRootLayout.setOnTouchListener { v, event ->
            val x = Math.round(event.x)
            val y = Math.round(event.y)
            if (isPlayGame && !isPause) {
                levelSurfaceView!!.touchOnScreen(x, y)
            }
            true
        }
    }

    override fun drawLevelTransition(level: Int) {
        levelSurfaceView?.let {
            spriteCache?.apply {
                getSprite(
                    R.drawable.black_background,
                    it.myCanvasWFixed.toFloat(),
                    it.myCanvasHFixed.toFloat()
                )?.let { bitmap ->
                    levelSurfaceView?.canvasView?.drawBitmap(
                        bitmap, 0f, 0f, null
                    )
                }
            }
        }
    }

    override fun showExitDialogFragment() {
        val exitDialogFragment = ExitDialogFragment.newInstance()
        if (!exitDialogFragment.isVisible) {
            exitDialogFragment.show(fragmentManager, DIALOG_TAG)
        }
    }

    override fun gameBegin() {
        super.gameBegin()
        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER)
        if (sensors.size > 0) {
            accelerometerSensor = AccelerometerSensor(this)
            sm.registerListener(accelerometerSensor, sensors[0], SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun showBackToMainMenuDialog(showDialog: Boolean) {
        if (showDialog) {
            if (backToMainMenuDialogFragment == null || backToMainMenuDialogFragment?.isVisible == false) {
                backToMainMenuDialogFragment = BackToMainMenuDialogFragment.newInstance()
                backToMainMenuDialogFragment?.show(fragmentManager, DIALOG_TAG)
            }
        }
    }

    override fun getGameOverText(): String {
        return getString(R.string.game_over)
    }

    override fun loadGameImagesResources() {
        val alienBaseSize = Util.horizontalDistanceAdjust(100, width)
        spriteCache?.getSprite(
            R.drawable.alien1,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien1_2,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien2,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien3,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien3_2,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien4,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien4_2,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        val asteroidBaseSize = Util.horizontalDistanceAdjust(120, width)
        spriteCache?.getSprite(
            R.drawable.asteroid,
            asteroidBaseSize.toFloat(),
            asteroidBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death1,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death2,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death3,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death4,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death5,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death6,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death7,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death8,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death9,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death10,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death11,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death12,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death13,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death14,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death15,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death16,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.death17,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        val smallFireBallSize = Util.horizontalDistanceAdjust(50, width)
        spriteCache?.getSprite(
            R.drawable.fireball1,
            smallFireBallSize.toFloat(),
            smallFireBallSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireball2,
            smallFireBallSize.toFloat(),
            smallFireBallSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireball3,
            smallFireBallSize.toFloat(),
            smallFireBallSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireball4,
            smallFireBallSize.toFloat(),
            smallFireBallSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireballsmall1,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireballsmall2,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireballsmall3,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.fireballsmall4,
            alienBaseSize.toFloat(),
            alienBaseSize.toFloat()
        )
        spriteCache?.getSprite(R.drawable.invisible, 10f, 10f)
        val shootWidthBaseSize = Util.horizontalDistanceAdjust(15, width)
        val shootHeightBaseSize = Util.horizontalDistanceAdjust(60, width)
        spriteCache?.getSprite(
            R.drawable.laser,
            shootWidthBaseSize.toFloat(),
            shootHeightBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.alien_shoot,
            shootWidthBaseSize.toFloat(),
            shootHeightBaseSize.toFloat()
        )
        val playerWidthBaseSize = Util.horizontalDistanceAdjust(70, width)
        val playerHeightBaseSize = Util.horizontalDistanceAdjust(140, width)
        spriteCache?.getSprite(
            R.drawable.navesmall,
            playerWidthBaseSize.toFloat(),
            playerHeightBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.shield,
            playerHeightBaseSize.toFloat(),
            playerHeightBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.shield_add,
            playerHeightBaseSize.toFloat(),
            playerHeightBaseSize.toFloat()
        )
        spriteCache?.getSprite(
            R.drawable.add_fireball,
            playerHeightBaseSize.toFloat(),
            playerHeightBaseSize.toFloat()
        )
    }

    companion object {
        private const val DIALOG_TAG = "Dialog"
    }
}