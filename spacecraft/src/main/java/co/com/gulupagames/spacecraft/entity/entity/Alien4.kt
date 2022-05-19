package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.entity.AlienBase

class Alien4(levelSurfaceView: LevelSurfaceView) : AlienBase(levelSurfaceView) {
    private val speedY: Int = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        y += speedY
        super.act()
        if (Math.random() < FIRING_FREQUENCY) {
            fire()
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

    fun fire() {
        levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.bomb)
        levelSurfaceView.addActor(
            AlienShoot2(
                levelSurfaceView,
                AlienShoot2.Companion.DOWN_RIGHT,
                x,
                y
            )
        )
        levelSurfaceView.addActor(AlienShoot2(levelSurfaceView, AlienShoot2.Companion.DOWN, x, y))
        levelSurfaceView.addActor(
            AlienShoot2(
                levelSurfaceView,
                AlienShoot2.Companion.DOWN_LEFT,
                x,
                y
            )
        )
        levelSurfaceView.addActor(AlienShoot2(levelSurfaceView, AlienShoot2.Companion.LEFT, x, y))
        levelSurfaceView.addActor(AlienShoot2(levelSurfaceView, AlienShoot2.Companion.RIGHT, x, y))
    }

    companion object {
        private const val FIRING_FREQUENCY = 0.06
        private const val BASE_SPEED = 6
        private const val PLUS_SCORE = 60
    }

    init {
        setRightSprites(intArrayOf(R.drawable.alien4))
        setLeftSprites(intArrayOf(R.drawable.alien4_2))
        frameDirectionAdjust()
    }
}