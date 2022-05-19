package co.com.gulupagames.spacecraft.entity.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.spacecraft.R

class Shield(levelSurfaceView: LevelSurfaceView) : GameEntity(levelSurfaceView) {
    override fun act() {
        updateFrame()
        if (levelSurfaceView.iGamePlayer!!.shields <= 100) {
            remove()
        } else {
            x = levelSurfaceView.iGamePlayer!!.x - levelSurfaceView.iGamePlayer!!.width / 2
            y = levelSurfaceView.iGamePlayer!!.y
        }
    }

    init {
        setRightSprites(intArrayOf(R.drawable.shield))
        frameSpeed = 5
        levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.death)
        frameDirectionAdjust()
    }
}