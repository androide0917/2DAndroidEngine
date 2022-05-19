package co.com.gulupagames.gamecore.game.surfaceview.helpers

import android.graphics.Canvas
import android.os.Message
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util

class PaintStatus(private val levelSurfaceView: LevelSurfaceView) {
    private var FPS = 0
    private var lastExecution: Long = 0
    private var start = false
    private var counter = 0
    fun paintStatus() {
        updateLevelPercent()
        if (levelSurfaceView.iGamePlayer != null) {
            paintShields()
            paintLives()
            paintBombs()
        }
        if (PAINT_FPS) {
            paintFPS(levelSurfaceView.canvasView)
        }
    }

    private fun updateLevelPercent() {
        if (levelSurfaceView.levelEngineCore?.levelPercent!! > 100) {
            levelSurfaceView.levelEngineCore?.levelPercent = 100.0
        }
        val msg = Message()
        msg.arg1 = levelSurfaceView.levelEngineCore?.levelPercent?.toInt() ?: 0
        levelSurfaceView.baseGameActivity?.updateLevelPercentHandler?.sendMessage(msg)
    }

    private fun paintShields() {
        if (levelSurfaceView.iGamePlayer?.shields ?: 0 < 0) {
            levelSurfaceView.iGamePlayer?.shields = 0
        }
        val msg = Message()
        msg.arg1 = levelSurfaceView.iGamePlayer?.shields ?: 0
        levelSurfaceView.baseGameActivity?.updateShieldsHandler?.sendMessage(msg)
    }

    private fun paintLives() {
        if (levelSurfaceView.lives < 0) {
            levelSurfaceView.lives = 0
        }
        val msg = Message()
        msg.arg1 = levelSurfaceView.lives
        levelSurfaceView.baseGameActivity!!.updateLivesHandler!!.sendMessage(msg)
    }

    private fun paintBombs() {
        if (levelSurfaceView.iGamePlayer?.clusterBombs ?: 0 < 0) {
            levelSurfaceView.iGamePlayer?.clusterBombs = 0
        }
        val msg = Message()
        msg.arg1 = levelSurfaceView.iGamePlayer?.clusterBombs ?: 0
        levelSurfaceView.baseGameActivity?.updateBombsHandler?.sendMessage(msg)
    }

    private fun paintFPS(canvas: Canvas?) {
        if (!start) {
            lastExecution = System.currentTimeMillis()
            start = true
        } else {
            if (System.currentTimeMillis() - lastExecution > 1000) {
                FPS = counter
                lastExecution = System.currentTimeMillis()
                counter = 0
            } else {
                counter++
            }
        }
        if (levelSurfaceView.iGamePlayer != null) {
            if (canvas != null) {
                Util.drawString(
                    canvas,
                    "FPS: $FPS",
                    SIDE_SEPARATOR,
                    40,
                    levelSurfaceView.baseGameActivity!!.getLevelTransitionFontColor(),
                    Util.verticalDistanceAdjust(25, levelSurfaceView.myCanvasHFixed)
                )
            }
        } else {
            if (canvas != null) {
                Util.drawString(
                    canvas,
                    "FPS: $FPS",
                    SIDE_SEPARATOR,
                    40,
                    levelSurfaceView.baseGameActivity!!.getLevelTransitionFontColor(),
                    Util.verticalDistanceAdjust(25, levelSurfaceView.myCanvasHFixed)
                )
            }
        }
    }

    companion object {
        private const val SIDE_SEPARATOR = 10
        private const val PAINT_FPS = true
    }
}