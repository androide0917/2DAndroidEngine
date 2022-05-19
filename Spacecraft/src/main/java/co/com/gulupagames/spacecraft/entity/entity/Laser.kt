package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R

class Laser(levelSurfaceView: LevelSurfaceView) : GameEntity(levelSurfaceView) {
    private val laserSpeed: Int = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        updateFrame()
        y -= laserSpeed
        if (y < 0) {
            remove()
            levelSurfaceView.iGamePlayer!!.setShootsInStage(-1)
        }
    }

    override fun collision(entity: GameEntity) {
        if (entity is AlienShoot || entity is AlienShoot2) {
            levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.explosion)
            levelSurfaceView.iGamePlayer!!.setShootsInStage(-1)
            entity.remove()
            remove()
        }
    }

    companion object {
        private const val BASE_SPEED = 15
    }

    init {
        setRightSprites(intArrayOf(R.drawable.laser))
        frameDirectionAdjust()
    }
}