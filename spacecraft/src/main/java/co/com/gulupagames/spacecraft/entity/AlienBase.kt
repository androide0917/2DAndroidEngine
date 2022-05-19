package co.com.gulupagames.spacecraft.entity

import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.entity.entity.AlienShoot

open class AlienBase protected constructor(levelSurfaceView: LevelSurfaceView?) :
    SpacecraftEntityBase(levelSurfaceView) {
    protected fun shoot1() {
        val m = AlienShoot(levelSurfaceView)
        m.x = (x + width / 2)
        m.y = (y + height)
        levelSurfaceView.addActor(m)
        levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.alien_shoot)
    }

    override fun act() {
        super.act()
        restrictBoundsExit()
        changeDirection()
    }

    private fun restrictBoundsExit() {
        if (x >= levelSurfaceView.myCanvasWFixed - width) {
            vx = -vx
        } else if (x <= 0 && vx < 0) {
            vx = -vx
        }
    }

    private fun changeDirection() {
        if (x < levelSurfaceView.iGamePlayer!!.x - width / 3 || x > levelSurfaceView.iGamePlayer!!.x + width / 3) {
            if (x > levelSurfaceView.iGamePlayer!!.x) {
                if (vx > 0) {
                    vx = -vx
                }
            } else if (x < levelSurfaceView.iGamePlayer!!.x) {
                if (vx < 0) {
                    vx = -vx
                }
            }
            x += vx
        }
    }
}