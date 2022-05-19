package co.com.gulupagames.gamecore.game.surfaceview.thread

import android.util.Log
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView

class LevelThread(private val parent: LevelSurfaceView) : Thread() {
    @Volatile
    var running = false
    private var sleepTime: Long
    override fun run() {
        while (running) {
            try {
                sleep(sleepTime)
                parent.updateSurfaceView()
            } catch (e: InterruptedException) {
                Log.e("ERROR", "Error updating SurfaceView", e)
            }
        }
    }

    fun setSleepTime(sleepTime: Long) {
        this.sleepTime = sleepTime
    }

    init {
        sleepTime = LevelSurfaceView.FPS_WAIT
    }
}