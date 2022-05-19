package co.com.gulupagames.gamecore.game.gameactivity

import android.app.Activity
import android.app.LoaderManager
import android.content.Loader
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import co.com.gulupagames.gamecore.R
import co.com.gulupagames.gamecore.cache.SoundCache
import co.com.gulupagames.gamecore.cache.SpriteCache
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.game.surfaceview.updateworldengine.ILevelEngine
import co.com.gulupagames.gamecore.model.CentralText
import co.com.gulupagames.gamecore.util.GameImagesResourceLoader
import dmax.dialog.SpotsDialog

abstract class BaseGameActivity : Activity(), LoaderManager.LoaderCallbacks<Any?> {
    var soundCache: SoundCache? = null
        protected set
    var spriteCache: SpriteCache? = null
        protected set
    var levelSurfaceView: LevelSurfaceView? = null
        protected set
    var isPlayGame = false
        protected set
    var isPause = false
    private var isInitMenu = false
    var levelEngine: ILevelEngine? = null
        protected set
    protected var mainMenuLayout: LinearLayout? = null
    protected var menuRelativeLayout: RelativeLayout? = null
    protected var centralTextView: TextView? = null
    protected var centralTextViewLayout: LinearLayout? = null
    protected var viewGroup: ViewGroup? = null
    protected var imageLoader: GameImagesResourceLoader? = null
    private var progressDialog: SpotsDialog? = null
    var width = 0
    var height = 0
    val changeCentralTextHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            centralTextViewLayout?.visibility = View.VISIBLE
            val centralText = msg.obj as CentralText
            centralTextView?.text = centralText.text
            val animation1 = AlphaAnimation(0.0f, 1.0f)
            animation1.duration = 1000
            centralTextViewLayout?.startAnimation(animation1)
        }
    }
    private val hideCentralTextHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            centralTextViewLayout?.visibility = View.GONE
        }
    }
    private val backToMainMenuHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            menuRelativeLayout?.visibility = RelativeLayout.VISIBLE
            isPlayGame = false
            runMainMenu()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        soundCache = SoundCache(this)
        spriteCache = SpriteCache(this)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels
        height = metrics.heightPixels
        progressDialog = SpotsDialog(this, getString(R.string.loadingImages))
        progressDialog?.setCancelable(false)
    }

    val baseGameActivity: BaseGameActivity
        get() = this

    protected open fun gameBegin() {
        soundCache?.startLoopSound()
        if (isPlayGame) {
            if (!isPause) {
                if (levelSurfaceView != null) {
                    resumeGame()
                } else {
                    runGame()
                }
            }
        } else if (!isInitMenu) {
            runMainMenu()
            val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            mainMenuLayout?.startAnimation(slideInLeft)
        }
    }

    fun resumeGame() {
        levelSurfaceView?.levelSurfaceViewOnResume()
        isPause = false
    }

    protected fun pauseGame(showDialog: Boolean) {
        levelSurfaceView?.levelSurfaceViewOnPause()
        isPause = true
        showBackToMainMenuDialog(showDialog)
    }

    fun backToMainMenu() {
        backToMainMenuHandler.sendEmptyMessage(0)
    }

    override fun onBackPressed() {
        if (isPlayGame) {
            pauseGame(true)
        } else {
            showExitDialogFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        soundCache?.stopLoopSound()
        if (isPlayGame) {
            pauseGame(true)
        }
    }

    override fun onResume() {
        super.onResume()
        loadImagesLoaderCall()
    }

    fun hideCentralText() {
        hideCentralTextHandler.sendMessage(Message())
    }

    open fun runMainMenu() {
        isPlayGame = false
        isInitMenu = true
    }

    open fun runGame() {
        isPlayGame = true
    }

    private fun loadImagesLoaderCall() {
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
        imageLoader?.forceLoad()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Any?>? {
        return when (id) {
            IMAGE_LOADER -> {
                imageLoader = GameImagesResourceLoader(this)
                imageLoader
            }
            else -> null
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onLoadFinished(loader: Loader<Any?>?, data: Any?) {
        when (loader?.id) {
            IMAGE_LOADER -> {
                loaderManager.restartLoader(IMAGE_LOADER, null, this)
                if (progressDialog?.isShowing == true) {
                    progressDialog?.dismiss()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onLoaderReset(loader: Loader<Any?>?) {
        //do nothing
    }

    protected abstract fun showExitDialogFragment()
    protected abstract fun showBackToMainMenuDialog(showDialog: Boolean)
    abstract fun getLoopSound(): MediaPlayer?
    abstract fun showControls()
    abstract fun hideControls()
    abstract fun drawLevelTransition(level: Int)
    abstract fun getLevelTransitionFontColor(): Int
    abstract fun getLevelBackGround(level: Int): Bitmap?
    abstract fun getLevelBackGroundAnimMode(level: Int): Enum<*>?
    abstract fun loadGameImagesResources()
    abstract fun getGameOverText(): String
    abstract val updateScoreHandler: Handler?
    abstract val updateLevelPercentHandler: Handler?
    abstract val updateShieldsHandler: Handler?
    abstract val updateLivesHandler: Handler?
    abstract val updateBombsHandler: Handler?

    companion object {
        const val IMAGE_LOADER = 1
    }
}