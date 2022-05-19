package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.horizontalDistanceAdjust
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R

class AlienShoot2(stage: LevelSurfaceView?, heading: Int, x: Int, y: Int) : GameEntity(
    stage!!
) {
    override fun act() {
        y += vy
        x += vx
        updateFrame()
        if (y < 0 - height || y > levelSurfaceView.myCanvasHFixed || x < 0 - width || x > levelSurfaceView.myCanvasWFixed) remove()
    }

    override fun collision(entity: GameEntity) {
        if (entity is Bomb) {
            levelSurfaceView.iGamePlayer!!.setShootsInStage(-1)
            remove()
        }
    }

    companion object {
        private const val UP_LEFT = 0
        private const val UP = 1
        private const val UP_RIGHT = 2
        const val LEFT = 3
        const val RIGHT = 4
        const val DOWN_LEFT = 5
        const val DOWN = 6
        const val DOWN_RIGHT = 7
        private const val BASE_BOMB_SPEED = 15
    }

    init {
        val baseBombSpeedY =
            verticalDistanceAdjust(BASE_BOMB_SPEED, levelSurfaceView.myCanvasHFixed)
        val baseBombSpeedX =
            horizontalDistanceAdjust(BASE_BOMB_SPEED, levelSurfaceView.myCanvasWFixed)
        when (heading) {
            UP_LEFT -> {
                vx = -baseBombSpeedX
                vy = -baseBombSpeedY
            }
            UP -> {
                vx = 0
                vy = -baseBombSpeedY
            }
            UP_RIGHT -> {
                vx = baseBombSpeedX
                vy = -baseBombSpeedY
            }
            LEFT -> {
                vx = -baseBombSpeedX
                vy = 0
            }
            RIGHT -> {
                vx = baseBombSpeedX
                vy = 0
            }
            DOWN_LEFT -> {
                vx = -baseBombSpeedX
                vy = baseBombSpeedY
            }
            DOWN -> {
                vx = 0
                vy = baseBombSpeedY
            }
            DOWN_RIGHT -> {
                vx = baseBombSpeedX
                vy = baseBombSpeedY
            }
        }
        setRightSprites(
            intArrayOf(
                R.drawable.fireballsmall1,
                R.drawable.fireballsmall2,
                R.drawable.fireballsmall3,
                R.drawable.fireballsmall4
            )
        )
        frameDirectionAdjust()
        this.x = x
        this.y = y
        frameSpeed = 3
    }
}