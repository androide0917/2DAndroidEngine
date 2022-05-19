package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.spacecraft.R

class Death(levelSurfaceView: LevelSurfaceView) : GameEntity(levelSurfaceView) {
    private val lastSprite: Int
    override fun act() {
        updateFrame()
        if (getSpriteNames()[currentFrame] == lastSprite) {
            levelSurfaceView.setDeath()
            remove()
        }
    }

    init {
        setRightSprites(
            intArrayOf(
                R.drawable.death1,
                R.drawable.death2,
                R.drawable.death3,
                R.drawable.death4,
                R.drawable.death5,
                R.drawable.death6,
                R.drawable.death7,
                R.drawable.death8,
                R.drawable.death9,
                R.drawable.death10,
                R.drawable.death11,
                R.drawable.death12,
                R.drawable.death13,
                R.drawable.death14,
                R.drawable.death15,
                R.drawable.death16,
                R.drawable.death17
            )
        )
        lastSprite = R.drawable.death17
        frameSpeed = 5
        levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.death)
        frameDirectionAdjust()
    }
}