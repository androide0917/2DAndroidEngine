package co.com.gulupagames.gamecore.game.surfaceview.helpers

import android.content.res.Resources
import android.os.Message
import co.com.gulupagames.gamecore.R
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.model.CentralText

class GameOver(private val levelSurfaceView: LevelSurfaceView) {
    private val resources: Resources = levelSurfaceView.baseGameActivity!!.baseGameActivity.resources
    var isGameOver = false
    var isGameOverPainted = false
    var isScorePainted = false
    fun paintGameOver() {
        paintBlack()
        val centralText =
            CentralText(levelSurfaceView.baseGameActivity!!.baseGameActivity.getGameOverText())
        val msg = Message()
        msg.obj = centralText
        levelSurfaceView.baseGameActivity!!.changeCentralTextHandler.sendMessage(msg)
        levelSurfaceView.levelThread!!.setSleepTime(LevelSurfaceView.CHANGE_LEVEL_PAUSE_TIME)
        isGameOverPainted = true
    }

    fun scorePaint() {
        paintBlack()
        val centralText =
            CentralText(resources.getString(R.string.score_final) + " " + levelSurfaceView.score)
        val msg = Message()
        msg.obj = centralText
        levelSurfaceView.baseGameActivity!!.changeCentralTextHandler.sendMessage(msg)
        levelSurfaceView.levelThread!!.setSleepTime(LevelSurfaceView.CHANGE_LEVEL_PAUSE_TIME)
        isScorePainted = true
    }

    fun paintBlack() {
        levelSurfaceView.canvasView!!.drawBitmap(
            levelSurfaceView.backGround!!.black!!,
            0f,
            0f,
            null
        )
    }

}