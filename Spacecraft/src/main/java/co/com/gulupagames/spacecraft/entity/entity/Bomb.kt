package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R

open class Bomb(levelSurfaceView: LevelSurfaceView, heading: Int, x: Int, y: Int) :
    GameEntity(levelSurfaceView) {
    protected val baseBombSpeedY: Int = verticalDistanceAdjust(BASE_BOMB_SPEED, levelSurfaceView.myCanvasHFixed)
    protected val baseBombSpeedX: Int = verticalDistanceAdjust(BASE_BOMB_SPEED, levelSurfaceView.myCanvasHFixed)
    override fun act() {
        updateFrame()
        y += vy
        x += vx
        if (y < 0 - height || y > levelSurfaceView.myCanvasHFixed || x < 0 - width || x > levelSurfaceView.myCanvasWFixed) remove()
    }

    companion object {
        const val UP_LEFT = 0
        const val UP = 1
        const val UP_RIGHT = 2
        const val LEFT = 3
        const val RIGHT = 4
        private const val DOWN_LEFT = 5
        private const val DOWN = 6
        private const val DOWN_RIGHT = 7
        protected const val BASE_BOMB_SPEED = 15
    }

    init {
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
                R.drawable.fireball1,
                R.drawable.fireball2,
                R.drawable.fireball3,
                R.drawable.fireball4
            )
        )
        frameDirectionAdjust()
        this.x = x
        this.y = y
        frameSpeed = 2
    }
}