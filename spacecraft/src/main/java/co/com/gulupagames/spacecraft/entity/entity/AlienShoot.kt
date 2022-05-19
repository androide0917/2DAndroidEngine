package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R

class AlienShoot(levelSurfaceView: LevelSurfaceView) : GameEntity(levelSurfaceView) {
    private val bulletSpeed: Int = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        y += bulletSpeed
        updateFrame()
    }

    override fun collision(entity: GameEntity) {
        if (entity is Bomb) {
            levelSurfaceView.iGamePlayer?.setShootsInStage(-1)
            remove()
        }
    }

    companion object {
        private const val BASE_SPEED = 15
    }

    init {
        setRightSprites(intArrayOf(R.drawable.alien_shoot))
        frameDirectionAdjust()
    }
}