package co.com.gulupagames.gamecore.game.surfaceview.helpers

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Message
import co.com.gulupagames.gamecore.R
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.model.CentralText

class BackgroundLooper(private val levelSurfaceView: LevelSurfaceView) {
    private val resources: Resources = levelSurfaceView.baseGameActivity!!.baseGameActivity.resources
    var black: Bitmap? = null
        private set
    private var loop = 0
    private var loopTmp = 0
    private var backGroundY = 0
    private var levelBackground: Bitmap? = null
    fun loadBackGround(): Bitmap? {
        black = levelSurfaceView.baseGameActivity!!.spriteCache!!.getSprite(
            R.drawable.black_background,
            levelSurfaceView.myCanvasWFixed.toFloat(),
            levelSurfaceView.myCanvasHFixed.toFloat()
        )
        levelBackground =
            levelSurfaceView.baseGameActivity!!.getLevelBackGround(levelSurfaceView.level)
        backGroundY = levelBackground!!.height
        loop = 0
        loopTmp = -backGroundY
        return levelBackground
    }

    fun playBackground() {
        if (levelSurfaceView.baseGameActivity!!.getLevelBackGroundAnimMode(levelSurfaceView.level) == BackGroundAnimMode.UP_TO_DOWN_ANIM) {
            loop += LOOPER
            loopTmp += LOOPER
            paintBackGround()
        } else if (levelSurfaceView.baseGameActivity!!.getLevelBackGroundAnimMode(levelSurfaceView.level) == BackGroundAnimMode.STATIC) {
            levelSurfaceView.canvasView!!.drawBitmap(levelBackground!!, 0f, 0f, null)
        }
    }

    private fun paintBackGround() {
        if (loopTmp >= backGroundY) {
            loopTmp -= backGroundY * 2
        }
        levelSurfaceView.canvasView!!.drawBitmap(levelBackground!!, 0f, loopTmp.toFloat(), null)
        if (loop >= backGroundY) {
            loop -= backGroundY * 2
        }
        levelSurfaceView.canvasView!!.drawBitmap(levelBackground!!, 0f, loop.toFloat(), null)
    }

    fun paintStage() {
        levelSurfaceView.canvasView?.drawBitmap(black!!, 0f, 0f, null)
        val centralText =
            CentralText(resources.getString(R.string.level) + " " + levelSurfaceView.level)
        val msg = Message()
        msg.obj = centralText
        levelSurfaceView.baseGameActivity?.changeCentralTextHandler?.sendMessage(msg)
        levelSurfaceView.levelThread?.setSleepTime(LevelSurfaceView.CHANGE_LEVEL_PAUSE_TIME)
    }

    fun paintReady() {
        levelSurfaceView.baseGameActivity?.drawLevelTransition(levelSurfaceView.level)
        val centralText = CentralText(resources.getString(R.string.ready))
        val msg = Message()
        msg.obj = centralText
        levelSurfaceView.baseGameActivity?.changeCentralTextHandler?.sendMessage(msg)
        levelSurfaceView.levelThread?.setSleepTime(LevelSurfaceView.CHANGE_LEVEL_PAUSE_TIME)
    }

    enum class BackGroundAnimMode {
        STATIC, UP_TO_DOWN_ANIM
    }

    companion object {
        private const val LOOPER = 1
    }

}