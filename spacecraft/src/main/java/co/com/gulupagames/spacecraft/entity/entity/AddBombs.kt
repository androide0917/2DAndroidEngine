package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.verticalDistanceAdjust
import co.com.gulupagames.spacecraft.R

class AddBombs(levelSurfaceView: LevelSurfaceView) : GameEntity(levelSurfaceView) {
    private val speedY: Int
    override fun act() {
        updateFrame()
        y += speedY
    }

    override fun collision(entity: GameEntity) {
        super.collision(entity)
        if (entity is GamePlayer) {
            remove()
            levelSurfaceView.iGamePlayer!!.addBombs(25)
        }
    }

    companion object {
        private const val BASE_SPEED = 15
    }

    init {
        setRightSprites(intArrayOf(R.drawable.add_fireball))
        frameSpeed = 5
        speedY = verticalDistanceAdjust(BASE_SPEED, levelSurfaceView.myCanvasHFixed)
        levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.add)
        frameDirectionAdjust()
    }
}