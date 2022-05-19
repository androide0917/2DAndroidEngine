package co.com.gulupagames.gamecore.accelerometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util

class AccelerometerSensor(private val baseGameActivity: BaseGameActivity) : SensorEventListener {
    private var lastUpdate: Long = 0
    private var isTablet = false
    override fun onSensorChanged(event: SensorEvent) {
        synchronized(this) {
            if (baseGameActivity.isPlayGame && baseGameActivity.levelSurfaceView != null) {
                val currentTime = event.timestamp
                val curX = event.values[0]
                val curY = event.values[1]
                val actualValue: Float = if (isTablet) {
                    -curX
                } else {
                    curY
                }
                val timeDifference = currentTime - lastUpdate
                if (timeDifference > 0) {
                    when {
                        actualValue >= 0.5 -> {
                            baseGameActivity.levelSurfaceView?.keyReleased(LevelSurfaceView.LEFT)
                            baseGameActivity.levelSurfaceView?.keyPressed(
                                LevelSurfaceView.RIGHT,
                                actualValue
                            )
                        }
                        actualValue <= -0.5 -> {
                            baseGameActivity.levelSurfaceView?.keyReleased(LevelSurfaceView.RIGHT)
                            baseGameActivity.levelSurfaceView?.keyPressed(
                                LevelSurfaceView.LEFT,
                                actualValue
                            )
                        }
                        else -> {
                            baseGameActivity.levelSurfaceView?.keyReleased(LevelSurfaceView.RIGHT)
                            baseGameActivity.levelSurfaceView?.keyReleased(LevelSurfaceView.LEFT)
                        }
                    }
                }
                lastUpdate = currentTime
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

    init {
        if (Util.isTablet(baseGameActivity.baseGameActivity)) {
            isTablet = true
        }
    }
}