package co.com.gulupagames.spacecraft.entity

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView

open class SpacecraftEntityBase(levelSurfaceView: LevelSurfaceView?) : GameEntity(
    levelSurfaceView!!
) {
    fun initVx(i: Int) {
        if (levelSurfaceView.iGamePlayer != null && x >= levelSurfaceView.iGamePlayer!!.x) {
            vx = -i
        }
        if (levelSurfaceView.iGamePlayer != null && x < levelSurfaceView.iGamePlayer!!.x) {
            vx = i
        }
    }
}