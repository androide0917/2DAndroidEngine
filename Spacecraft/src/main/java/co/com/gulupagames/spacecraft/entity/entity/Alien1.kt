package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.entity.AlienBase

class Alien1(levelSurfaceView: LevelSurfaceView) : AlienBase(levelSurfaceView) {
    private val speedY: Int = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        super.act()
        y += speedY
        if (Math.random() < FIRING_FREQUENCY) {
            shoot1()
        }
    }

    override fun collision(entity: GameEntity) {
        if (entity is Laser || entity is Bomb) {
            levelSurfaceView.baseGameActivity?.soundCache?.playSound(R.raw.explosion)
            remove()
            if (entity is Laser) {
                entity.remove()
                levelSurfaceView.iGamePlayer?.setShootsInStage(-1)
            }
            levelSurfaceView.iGamePlayer?.addScore(PLUS_SCORE, entity)
        }
    }

    companion object {
        private const val FIRING_FREQUENCY = 0.01
        private const val BASE_SPEED = 6
        private const val PLUS_SCORE = 20
    }

    init {
        setRightSprites(intArrayOf(R.drawable.alien1))
        setLeftSprites(intArrayOf(R.drawable.alien1_2))
        frameDirectionAdjust()
    }
}