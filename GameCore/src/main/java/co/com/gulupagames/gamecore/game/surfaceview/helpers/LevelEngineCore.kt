package co.com.gulupagames.gamecore.game.surfaceview.helpers

import android.util.Log
import android.util.SparseArray
import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.LineCounter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.CopyOnWriteArrayList

class LevelEngineCore(val levelSurfaceView: LevelSurfaceView) {
    var maxLevel = 0
    private var actualLevel = 0
    private var lastExecution: Long = 0
    private var lineCounter = 0.0
    var levelPercent = 0.0
        get() {
            val tmp = lineCounter / levels!![actualLevel].maxLines
            field = tmp * 100
            return field
        }
    private var inStream: InputStream? = null
    private var inputReader: InputStreamReader? = null
    private var buffReader: BufferedReader? = null
    private var levels: SparseArray<LineCounter>? = null
    fun initLevels() {
        levelSurfaceView.baseGameActivity!!.levelEngine!!.initLevels(this)
    }

    fun updateWorld() {
        val newActors = loadLevelEngine()
        newActors?.let {
            levelSurfaceView.addActors(it)
        }
        levelSurfaceView.canvasView?.let {
            levelSurfaceView.iGamePlayer?.act()
            levelSurfaceView.iGamePlayer?.paintCanvasView(it)
            for (m in levelSurfaceView.actors!!) {
                m.act()
                m.paintCanvasView(it)
            }
        }

    }

    private fun loadLevelEngine(): MutableList<GameEntity>? {
        try {
            if (inStream != null) {
                if (validateExecution()) {
                    val line = buffReader?.readLine()
                    lineCounter++
                    if (line != null) {
                        return levelSurfaceView.baseGameActivity?.levelEngine?.loadData(
                            line,
                            this
                        )
                    } else {
                        levelSurfaceView.setPassWord(true)
                    }
                }
            } else {
                initLevel(levelSurfaceView.level)
            }
        } catch (e: Exception) {
            restart()
        }
        return CopyOnWriteArrayList()
    }

    private fun validateExecution(): Boolean {
        var execute = false
        if (lastExecution == 0L) {
            execute = true
            lastExecution = System.currentTimeMillis()
        }
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastExecution > EXECUTION_TIME) {
            execute = true
            lastExecution = System.currentTimeMillis()
        }
        return execute
    }

    fun initLevel(level: Int) {
        try {
            inStream = levels!![level].inputStream
            actualLevel = level
            lineCounter = 0.0
            inputReader = InputStreamReader(inStream)
            buffReader = BufferedReader(inputReader)
        } catch (ex: Exception) {
            Log.e("ERROR", "Error trying to reach the level: " + level + ", error: " + ex.message)
        }
    }

    fun restart() {
        if (buffReader != null) {
            try {
                lineCounter = 0.0
                inStream!!.reset()
                inputReader = InputStreamReader(inStream)
                buffReader = BufferedReader(inputReader)
            } catch (ex: IOException) {
                Log.e("ERROR", "Error trying to restart the reader, error : " + ex.message)
            }
        }
    }

    fun setLevels(levels: SparseArray<LineCounter>?) {
        this.levels = levels
    }

    companion object {
        private const val EXECUTION_TIME: Long = 1000
    }
}