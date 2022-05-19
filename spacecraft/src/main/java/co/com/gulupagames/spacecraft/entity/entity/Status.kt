package co.com.gulupagames.spacecraft.entity.entity

import android.graphics.Canvas
import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.drawString
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R

class Status(
    levelSurfaceView: LevelSurfaceView?,
    actor: GameEntity?,
    private val message: String,
    color: Int
) : GameEntity(
    levelSurfaceView!!
) {
    private val creationDate: Long
    private val color: Int
    override fun act() {
        if (System.currentTimeMillis() - creationDate > STANDARD_DELETE_TIME) {
            remove()
        }
    }

    override fun paintCanvasView(canvas: Canvas) {
        drawString(
            canvas,
            message,
            x,
            y,
            color,
            verticalDistanceAdjust(30, levelSurfaceView.myCanvasHFixed)
        )
    }

    companion object {
        private const val STANDARD_DELETE_TIME: Long = 300
    }

    init {
        creationDate = System.currentTimeMillis()
        this.color = color
        if (actor != null) {
            x = actor.x+ actor.width / 2
        }
        if (actor != null) {
            y = actor.y
        }
        setRightSprites(intArrayOf(R.drawable.invisible))
        frameDirectionAdjust()
    }
}