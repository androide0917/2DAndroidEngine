package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.entity.SpacecraftEntityBase

class Asteroid(stage: LevelSurfaceView?) : SpacecraftEntityBase(stage) {
    private val speedY: Int = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        updateFrame()
        x += vx
        y += speedY
        if (y > levelSurfaceView.myCanvasHFixed + height) {
            remove()
        }
    }

    override fun collision(entity: GameEntity) {
        if (entity is Laser) {
            entity.remove()
            levelSurfaceView.iGamePlayer!!.setShootsInStage(-1)
        }
        if (entity is Bomb) {
            levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.explosion)
            entity.remove()
            remove()
            levelSurfaceView.iGamePlayer!!.addScore(PLUS_SCORE, entity)
        }
    }

    companion object {
        private const val BASE_SPEED = 10
        private const val PLUS_SCORE = 100
    }

    init {
        setRightSprites(intArrayOf(R.drawable.asteroid))
        frameDirectionAdjust()
    }
}