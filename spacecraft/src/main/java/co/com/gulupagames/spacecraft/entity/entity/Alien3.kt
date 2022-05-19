package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.entity.AlienBase

class Alien3(stage: LevelSurfaceView?) : AlienBase(stage) {
    private val speedY: Int = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        y += speedY
        super.act()
        if (Math.random() < FIRING_FREQUENCY) {
            shoot1()
        }
    }

    override fun collision(entity: GameEntity) {
        if (entity is Laser || entity is Bomb) {
            levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.explosion)
            remove()
            levelSurfaceView.iGamePlayer!!.setShootsInStage(-1)
            (entity as? Laser)?.remove()
            levelSurfaceView.iGamePlayer!!.addScore(PLUS_SCORE, entity)
        }
    }

    companion object {
        protected const val FIRING_FREQUENCY = 0.06
        private const val BASE_SPEED = 10
        private const val PLUS_SCORE = 60
    }

    init {
        setRightSprites(intArrayOf(R.drawable.alien3))
        setLeftSprites(intArrayOf(R.drawable.alien3_2))
        frameDirectionAdjust()
    }
}